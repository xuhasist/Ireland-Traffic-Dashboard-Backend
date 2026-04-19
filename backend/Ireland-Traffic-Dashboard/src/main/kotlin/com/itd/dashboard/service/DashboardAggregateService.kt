package com.itd.dashboard.service

import com.itd.dashboard.dto.DashboardAggregateResponseDto
import com.itd.dashboard.dto.DashboardChartsRequestDto
import com.itd.dashboard.dto.DashboardSummaryRequestDto
import com.itd.location.service.CityConfigService
import com.itd.traffic.dto.RoadPointRequest
import com.itd.traffic.service.TomTomProxyService
import com.itd.weather.service.OpenWeatherProxyService
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import java.time.Instant
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Service
class DashboardAggregateService(
    private val cityConfigService: CityConfigService,
    private val tomTomProxyService: TomTomProxyService,
    private val openWeatherProxyService: OpenWeatherProxyService,
    private val dashboardSummaryService: DashboardSummaryService,
    private val dashboardChartsService: DashboardChartsService,
) {

    fun getDashboard(city: String, mode: String): DashboardAggregateResponseDto {
        val normalizedMode = mode.lowercase()
        if (normalizedMode != "live") {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Dashboard aggregate API currently supports live mode only.",
            )
        }

        val cityConfig = cityConfigService.getEnabledCityConfig(city)
            ?: throw ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "City config not found for city='${city}'",
            )

        val roads = cityConfig.roads.map {
            RoadPointRequest(
                name = it.name,
                lat = it.lat,
                lng = it.lng,
            )
        }

        val traffic = tomTomProxyService.fetchTrafficFlow(roads).results
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

        val updatedAt = formatUpdatedAt(Instant.now(), weather?.timezone) ?: DEFAULT_TIME_LABEL

        val metrics = dashboardSummaryService.buildSummary(
            DashboardSummaryRequestDto(
                traffic = traffic,
                incidents = incidents,
                filteredIncidentCount = incidents.size,
                updatedAt = updatedAt,
                previousAvgSpeed = null,
                jamThreshold = MODERATE_JAM_THRESHOLD,
            )
        )

        val charts = dashboardChartsService.buildCharts(
            DashboardChartsRequestDto(
                traffic = traffic,
                previousSpeedTrend = emptyList(),
                timeLabel = updatedAt,
                goodThreshold = GOOD_JAM_THRESHOLD,
                moderateThreshold = MODERATE_JAM_THRESHOLD,
                maxPoints = DEFAULT_MAX_TREND_POINTS,
                yMax = DEFAULT_Y_MAX,
            )
        )

        return DashboardAggregateResponseDto(
            city = cityConfig.cityName,
            mode = normalizedMode,
            weather = weather,
            traffic = traffic,
            incidents = incidents,
            metrics = metrics,
            charts = charts,
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

    companion object {
        private const val GOOD_JAM_THRESHOLD = 4.0
        private const val MODERATE_JAM_THRESHOLD = 5.0
        private const val DEFAULT_MAX_TREND_POINTS = 10
        private const val DEFAULT_Y_MAX = 60
        private const val DEFAULT_TIME_LABEL = "now"
    }
}

