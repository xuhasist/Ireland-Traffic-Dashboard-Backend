package com.itd.location.service

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.itd.location.dto.BBoxDto
import com.itd.location.dto.CityConfigItemDto
import com.itd.location.dto.CityConfigListResponseDto
import com.itd.location.dto.RoadPointDto
import com.itd.location.repository.CityConfigRepository
import org.springframework.stereotype.Service

@Service
class CityConfigService(
    private val cityConfigRepository: CityConfigRepository,
    private val objectMapper: ObjectMapper,
) {
    private val roadListType = object : TypeReference<List<RoadPointDto>>() {}

    fun getEnabledCityConfigs(): CityConfigListResponseDto {
        val items = cityConfigRepository.findAllByEnabledTrueOrderByCityNameAsc()
            .map { entity ->
                CityConfigItemDto(
                    cityName = entity.cityName,
                    center = listOf(entity.centerLat, entity.centerLng),
                    bbox = BBoxDto(
                        minLon = entity.bboxMinLon,
                        minLat = entity.bboxMinLat,
                        maxLon = entity.bboxMaxLon,
                        maxLat = entity.bboxMaxLat,
                    ),
                    roads = parseRoads(entity.roadsJson),
                )
            }

        return CityConfigListResponseDto(data = items)
    }

    fun getEnabledCityConfig(cityName: String): CityConfigItemDto? {
        val entity = cityConfigRepository.findByCityNameIgnoreCaseAndEnabledTrue(cityName)
            ?: return null

        return CityConfigItemDto(
            cityName = entity.cityName,
            center = listOf(entity.centerLat, entity.centerLng),
            bbox = BBoxDto(
                minLon = entity.bboxMinLon,
                minLat = entity.bboxMinLat,
                maxLon = entity.bboxMaxLon,
                maxLat = entity.bboxMaxLat,
            ),
            roads = parseRoads(entity.roadsJson),
        )
    }

    private fun parseRoads(json: String): List<RoadPointDto> {
        return try {
            objectMapper.readValue(json, roadListType)
        } catch (ex: Exception) {
            emptyList()
        }
    }
}