package com.itd.traffic.service

import com.fasterxml.jackson.databind.JsonNode
import com.itd.traffic.dto.FlowLinkDto
import com.itd.traffic.dto.FlowPointDto
import com.itd.traffic.dto.IncidentDetailsDto
import com.itd.traffic.dto.IncidentDto
import com.itd.traffic.dto.IncidentImpactDto
import com.itd.traffic.dto.IncidentLocationDto
import com.itd.traffic.dto.IncidentResponseDto
import com.itd.traffic.dto.RoadPointRequest
import com.itd.traffic.dto.TrafficCurrentFlowDto
import com.itd.traffic.dto.TrafficFlowResponseDto
import com.itd.traffic.dto.TrafficLocationDto
import com.itd.traffic.dto.TrafficSegmentDto
import com.itd.traffic.dto.TrafficShapeDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Service
class TomTomProxyService(
    private val restClientBuilder: RestClient.Builder,
    @Value("\${external.tomtom.api-key}") private val apiKey: String,
    @Value("\${external.tomtom.base-url}") private val baseUrl: String,
) {
    private val restClient: RestClient = restClientBuilder.build()

    companion object {
        private val log = LoggerFactory.getLogger(TomTomProxyService::class.java)
    }

    @Cacheable(
        cacheNames = ["trafficFlow"],
        key = "T(com.itd.traffic.support.CacheKeyFactory).roadsKey(#roads)"
    )
    fun fetchTrafficFlow(roads: List<RoadPointRequest>): TrafficFlowResponseDto {
        if (apiKey.isBlank()) return TrafficFlowResponseDto(results = emptyList())

        val results = roads.mapNotNull { road ->
            fetchFlowForPoint(road)
        }

        return TrafficFlowResponseDto(results = results)
    }

    @Cacheable(
        cacheNames = ["trafficIncidents"],
        key = "T(com.itd.traffic.support.CacheKeyFactory).bboxKey(#minLon,#minLat,#maxLon,#maxLat)"
    )
    fun fetchIncidents(
        minLon: Double,
        minLat: Double,
        maxLon: Double,
        maxLat: Double,
    ): IncidentResponseDto {
        if (apiKey.isBlank()) return IncidentResponseDto(results = emptyList())

        val fields = "{incidents{type,geometry{type,coordinates},properties{id,iconCategory,magnitudeOfDelay,events{description,code,iconCategory},startTime,endTime,from,to,length,delay,roadNumbers,timeValidity,probabilityOfOccurrence,numberOfReports,lastReportTime,tmc{countryCode,tableNumber,tableVersion,direction,points{location,offset}}}}}";

        val uri = UriComponentsBuilder
            .fromUriString("$baseUrl/5/incidentDetails")
            .queryParam("key", apiKey)
            .queryParam("bbox", "$minLon,$minLat,$maxLon,$maxLat")
            .queryParam("language", "en-GB")
            .queryParam("fields", fields)
            .build()
            .encode()
            .toUri()

        val root = restClient.get()
            .uri(uri)
            .retrieve()
            .body(JsonNode::class.java)
            ?: return IncidentResponseDto(results = emptyList())

        val incidents = root.path("incidents")
        if (!incidents.isArray) {
            return IncidentResponseDto(results = emptyList())
        }

        val results = incidents.map { incident ->
            val properties = incident.path("properties")
            val geometry = incident.path("geometry")
            val iconCategory = properties.path("iconCategory").asInt(0)

            val points = geometry.path("coordinates")
                .mapNotNull { coord ->
                    if (coord.isArray && coord.size() >= 2) {
                        FlowPointDto(
                            lat = coord[1].asDouble(),
                            lng = coord[0].asDouble(),
                        )
                    } else {
                        null
                    }
                }

            val description = buildString {
                val from = properties.path("from").asText("")
                val to = properties.path("to").asText("")
                append(from)
                if (from.isNotBlank() && to.isNotBlank()) append(" to ")
                append(to)
            }.ifBlank { "Unknown road" }

            IncidentDto(
                incidentDetails = IncidentDetailsDto(
                    id = properties.path("id").asText(""),
                    type = getIncidentType(iconCategory),
                    criticality = getSeverityLevel(iconCategory),
                    description = properties.path("events").firstOrNull()?.path("description")?.asText("")
                        ?: "Unknown incident",
                    startTime = properties.path("startTime").asText(""),
                    endTime = properties.path("endTime").asText(""),
                ),
                location = IncidentLocationDto(
                    shape = TrafficShapeDto(
                        links = listOf(
                            FlowLinkDto(points = points)
                        )
                    ),
                    description = description,
                ),
                impact = IncidentImpactDto(
                    delayInSeconds = properties.path("delay").asInt(0),
                    affectedRoads = properties.path("roadNumbers").map { it.asText() },
                ),
                icon = getIncidentIcon(iconCategory),
            )
        }

        return IncidentResponseDto(results = results)
    }

    private fun fetchFlowForPoint(road: RoadPointRequest): TrafficSegmentDto? {
        val url = UriComponentsBuilder
            .fromHttpUrl("$baseUrl/4/flowSegmentData/absolute/22/json")
            .queryParam("point", "${road.lat},${road.lng}")
            .queryParam("unit", "KMPH")
            .queryParam("key", apiKey)
            .build(true)
            .toUriString()

        val root = try {
            restClient.get()
                .uri(url)
                .retrieve()
                .body(JsonNode::class.java)
        } catch (ex: HttpClientErrorException.BadRequest) {
            log.warn(
                "Skip TomTom flow point '{}': lat={}, lng={}, reason={}",
                road.name,
                road.lat,
                road.lng,
                ex.responseBodyAsString
            )
            return null
        } catch (ex: Exception) {
            log.error(
                "Failed to fetch TomTom flow point '{}': lat={}, lng={}",
                road.name,
                road.lat,
                road.lng,
                ex
            )
            return null
        } ?: return null

        val flow = root.path("flowSegmentData")
        if (flow.isMissingNode || flow.isNull) return null

        val freeFlow = flow.path("freeFlowSpeed").asDouble(0.0)
        val currentSpeed = flow.path("currentSpeed").asDouble(0.0)
        val speedDiff = freeFlow - currentSpeed
        val jamFactor = if (freeFlow > 0) (speedDiff / freeFlow) * 10 else 0.0

        val points = flow.path("coordinates").path("coordinate")
            .mapNotNull { coord ->
                val latNode = coord.path("latitude")
                val lngNode = coord.path("longitude")

                if (latNode.isNumber && lngNode.isNumber) {
                    FlowPointDto(
                        lat = latNode.asDouble(),
                        lng = lngNode.asDouble(),
                    )
                } else {
                    null
                }
            }

        return TrafficSegmentDto(
            location = TrafficLocationDto(
                description = road.name,
                shape = TrafficShapeDto(
                    links = listOf(
                        FlowLinkDto(points = if (points.size >= 2) points else emptyList())
                    )
                )
            ),
            currentFlow = TrafficCurrentFlowDto(
                speed = currentSpeed.roundToInt(),
                freeFlow = freeFlow.roundToInt(),
                jamFactor = clamp(jamFactor),
                traversability = if (flow.path("roadClosure").asBoolean(false)) "closed" else "open",
            ),
        )
    }

    private fun clamp(value: Double): Double {
        return ((max(0.0, min(10.0, value)) * 10).roundToInt()) / 10.0
    }

    private fun getIncidentIcon(iconCategory: Int): String {
        val iconMap = mapOf(
            0 to "❓",
            1 to "💥",
            2 to "🌫️",
            3 to "⚠️",
            4 to "🌧️",
            5 to "🧊",
            6 to "⚡",
            7 to "🚧",
            8 to "⛔",
            9 to "🏗️",
            10 to "💨",
            11 to "🌊",
            14 to "❌",
        )
        return iconMap[iconCategory] ?: ""
    }

    private fun getIncidentType(iconCategory: Int): String {
        val typeMap = mapOf(
            0 to "Unknown",
            1 to "Accident",
            2 to "Fog",
            3 to "Dangerous Conditions",
            4 to "Rain",
            5 to "Ice",
            6 to "Heavy Traffic",
            7 to "Lane Closed",
            8 to "Road Closed",
            9 to "Road Works",
            10 to "Wind",
            11 to "Flooding",
            14 to "Broken Down Vehicle",
        )
        return typeMap[iconCategory] ?: "Unknown"
    }

    private fun getSeverityLevel(iconCategory: Int): String {
        val typeMap = mapOf(
            0 to "Unknown",
            1 to "major",
            2 to "moderate",
            3 to "moderate",
            4 to "minor",
            5 to "major",
            6 to "minor",
            7 to "moderate",
            8 to "major",
            9 to "moderate",
            10 to "moderate",
            11 to "major",
            14 to "moderate",
        )
        return typeMap[iconCategory] ?: "Unknown"
    }
}