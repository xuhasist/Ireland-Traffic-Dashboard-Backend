package com.itd.snapshot.service

import com.itd.dashboard.dto.DashboardChartsRequestDto
import com.itd.dashboard.dto.DashboardSummaryRequestDto
import com.itd.dashboard.service.DashboardChartsService
import com.itd.dashboard.service.DashboardSummaryService
import com.itd.location.service.CityConfigService
import com.itd.snapshot.document.DashboardSnapshotDocument
import com.itd.snapshot.dto.*
import com.itd.snapshot.repository.DashboardSnapshotRepository
import com.itd.traffic.dto.RoadPointRequest
import com.itd.traffic.service.TomTomProxyService
import com.itd.weather.service.OpenWeatherProxyService
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class DashboardSnapshotService(
    private val dashboardSnapshotRepository: DashboardSnapshotRepository,
    private val cityConfigService: CityConfigService,
    private val tomTomProxyService: TomTomProxyService,
    private val openWeatherProxyService: OpenWeatherProxyService,
    private val dashboardSummaryService: DashboardSummaryService,
    private val dashboardChartsService: DashboardChartsService,
) {

    fun saveSnapshot(request: DashboardSnapshotSaveRequestDto): DashboardSnapshotResponseDto {
        if (request.dataMode != "live") {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Snapshot save only supports live mode because the backend now assembles the snapshot from live sources.",
            )
        }

        val cityConfig = cityConfigService.getEnabledCityConfig(request.city)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "City config not found for city='${request.city}'",
            )

        val roads = cityConfig.roads.map {
            RoadPointRequest(
                name = it.name,
                lat = it.lat,
                lng = it.lng,
            )
        }

        val traffic = tomTomProxyService.fetchTrafficFlow(roads).results.filterNotNull()

        val incidents = tomTomProxyService.fetchIncidents(
            minLon = cityConfig.bbox.minLon,
            minLat = cityConfig.bbox.minLat,
            maxLon = cityConfig.bbox.maxLon,
            maxLat = cityConfig.bbox.maxLat,
        ).results

        val weather = openWeatherProxyService.fetchWeather(
            lat = cityConfig.center[0],
            lon = cityConfig.center[1],
        )

        val generatedAt = Instant.now()
        val updatedAt = formatUpdatedAt(generatedAt, weather?.timezone)

        val latestSnapshot = dashboardSnapshotRepository.findTopByCityOrderByCapturedAtDesc(request.city)
        val previousAvgSpeed = latestSnapshot?.metrics?.avgSpeed

        val summary = dashboardSummaryService.buildSummary(
            DashboardSummaryRequestDto(
                traffic = traffic,
                incidents = incidents,
                filteredIncidentCount = incidents.size,
                updatedAt = updatedAt ?: DEFAULT_TIME_LABEL,
                previousAvgSpeed = previousAvgSpeed,
                jamThreshold = DEFAULT_JAM_THRESHOLD,
            )
        )

        val charts = dashboardChartsService.buildCharts(
            DashboardChartsRequestDto(
                traffic = traffic,
                previousSpeedTrend = emptyList(),
                timeLabel = updatedAt ?: DEFAULT_TIME_LABEL,
                goodThreshold = GOOD_JAM_THRESHOLD,
                moderateThreshold = MODERATE_JAM_THRESHOLD,
                maxPoints = DEFAULT_MAX_TREND_POINTS,
                yMax = DEFAULT_Y_MAX,
            )
        )

        val saved = dashboardSnapshotRepository.save(
            DashboardSnapshotDocument(
                city = request.city,
                dataMode = request.dataMode,
                trafficCount = traffic.size,
                incidentCount = incidents.size,
                generatedAt = generatedAt,
                weather = weather?.toSnapshotWeatherDto(),
                metrics = summary.toSnapshotMetricsDto(activeIncidentsTotal = incidents.size),
                congestion = charts.congestion.toSnapshotCongestionDto(),
            )
        )

        return DashboardSnapshotResponseDto(
            data = saved.toItemDto()
        )
    }

    fun getLatestSnapshot(city: String): DashboardSnapshotResponseDto? {
        val latest = dashboardSnapshotRepository.findTopByCityOrderByCapturedAtDesc(city)
            ?: return null

        return DashboardSnapshotResponseDto(
            data = latest.toItemDto()
        )
    }

    fun getRecentSnapshots(city: String, limit: Int): DashboardSnapshotListResponseDto {
        val safeLimit = limit.coerceIn(1, 100)

        val items = dashboardSnapshotRepository.findByCityOrderByCapturedAtDesc(
            city,
            PageRequest.of(0, safeLimit),
        )

        return DashboardSnapshotListResponseDto(
            data = items.map { it.toItemDto() }
        )
    }

    private fun formatUpdatedAt(now: Instant, timezone: String?): String? {
        val zoneId = timezone
            ?.toIntOrNull()
            ?.let { ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(it)) }
            ?: ZoneId.of("UTC")

        return DateTimeFormatter.ofPattern("HH:mm:ss")
            .withZone(zoneId)
            .format(now)
    }

    private fun com.itd.weather.dto.WeatherResponseDto.toSnapshotWeatherDto(): SnapshotWeatherDto {
        return SnapshotWeatherDto(
            temperature = this.temperature,
            description = this.description,
            icon = this.icon,
            dt = this.dt,
            timezone = this.timezone,
        )
    }

    private fun com.itd.dashboard.dto.DashboardSummaryDto.toSnapshotMetricsDto(
        activeIncidentsTotal: Int,
    ): SnapshotMetricsDto {
        return SnapshotMetricsDto(
            avgSpeed = this.avgSpeedKph,
            commuteTime = this.commuteTimeMinutes?.toDouble(),
            congestedRoads = this.congestedRoadCount,
            activeIncidentsFiltered = this.activeIncidentCount,
            activeIncidentsTotal = activeIncidentsTotal,
            avgJam = this.avgJamFactor,
            healthScore = this.healthScore?.toDouble(),
            updatedAt = this.updatedAt,
            jamThreshold = this.jamThreshold,
            trend = this.trend?.let {
                SnapshotTrendDto(
                    text = it.text,
                    dir = it.dir,
                )
            },
        )
    }

    private fun com.itd.dashboard.dto.CongestionBreakdownDto.toSnapshotCongestionDto(): SnapshotCongestionDto {
        return SnapshotCongestionDto(
            good = this.good,
            moderate = this.moderate,
            heavy = this.heavy,
        )
    }

    private fun DashboardSnapshotDocument.toItemDto(): DashboardSnapshotItemDto {
        return DashboardSnapshotItemDto(
            id = this.id ?: "",
            city = this.city,
            dataMode = this.dataMode,
            trafficCount = this.trafficCount,
            incidentCount = this.incidentCount,
            generatedAt = this.generatedAt.toString(),
            capturedAt = this.capturedAt.toString(),
            weather = this.weather,
            metrics = this.metrics,
            congestion = this.congestion,
        )
    }

    companion object {
        private const val DEFAULT_JAM_THRESHOLD = 5.0
        private const val GOOD_JAM_THRESHOLD = 4.0
        private const val MODERATE_JAM_THRESHOLD = 7.0
        private const val DEFAULT_MAX_TREND_POINTS = 10
        private const val DEFAULT_Y_MAX = 60
        private const val DEFAULT_TIME_LABEL = "now"
    }
}