package com.itd.dashboard.service

import com.itd.dashboard.dto.DashboardSummaryDto
import com.itd.dashboard.dto.DashboardSummaryRequestDto
import com.itd.dashboard.dto.MetricTrendDto
import org.springframework.stereotype.Service
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Service
class DashboardSummaryService {

    fun buildSummary(request: DashboardSummaryRequestDto): DashboardSummaryDto {
        if (request.traffic.isEmpty()) {
            return DashboardSummaryDto(
                avgSpeedKph = null,
                commuteTimeMinutes = null,
                congestedRoadCount = null,
                activeIncidentCount = null,
                avgJamFactor = null,
                healthScore = null,
                jamThreshold = request.jamThreshold,
                updatedAt = request.updatedAt,
                trend = null,
            )
        }

        val avgSpeed = request.traffic
            .map { it.currentFlow.speed.toDouble() }
            .average()

        val congestedRoadCount = request.traffic.count {
            it.currentFlow.jamFactor >= request.jamThreshold
        }

        val avgJamFactor = request.traffic
            .map { it.currentFlow.jamFactor }
            .average()

        val healthScore = clamp((100 - avgJamFactor * 10).roundToInt(), 0, 100)

        val trend = buildTrend(
            previousAvgSpeed = request.previousAvgSpeed,
            currentAvgSpeed = avgSpeed,
        )

        return DashboardSummaryDto(
            avgSpeedKph = round1(avgSpeed),
            commuteTimeMinutes = congestedRoadCount * 5,
            congestedRoadCount = congestedRoadCount,
            activeIncidentCount = request.filteredIncidentCount,
            avgJamFactor = round1(avgJamFactor),
            healthScore = healthScore,
            jamThreshold = request.jamThreshold,
            updatedAt = request.updatedAt,
            trend = trend,
        )
    }

    private fun buildTrend(
        previousAvgSpeed: Double?,
        currentAvgSpeed: Double,
    ): MetricTrendDto? {
        if (previousAvgSpeed == null) return null

        val diff = currentAvgSpeed - previousAvgSpeed
        val pct = if (previousAvgSpeed == 0.0) {
            0.0
        } else {
            (diff / previousAvgSpeed) * 100
        }

        return MetricTrendDto(
            text = "${if (diff >= 0) "▲" else "▼"} ${kotlin.math.abs(pct).roundToInt()}%",
            dir = if (diff >= 0) "up" else "down",
        )
    }

    private fun round1(value: Double): Double {
        return ((value * 10).roundToInt()) / 10.0
    }

    private fun clamp(value: Int, minValue: Int, maxValue: Int): Int {
        return max(minValue, min(maxValue, value))
    }
}

