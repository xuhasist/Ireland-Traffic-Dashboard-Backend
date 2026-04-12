package com.itd.dashboard.service

import com.itd.dashboard.dto.CongestionBreakdownDto
import com.itd.dashboard.dto.DashboardChartsDto
import com.itd.dashboard.dto.DashboardChartsRequestDto
import com.itd.dashboard.dto.SpeedTrendPointDto
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class DashboardChartsService {

    fun buildCharts(request: DashboardChartsRequestDto): DashboardChartsDto {
        val congestion = buildCongestionBreakdown(
            jamFactors = request.traffic.map { it.currentFlow.jamFactor },
            goodThreshold = request.goodThreshold,
            moderateThreshold = request.moderateThreshold,
        )

        val speedTrend = buildSpeedTrend(
            previousSpeedTrend = request.previousSpeedTrend,
            currentTraffic = request.traffic,
            timeLabel = request.timeLabel,
            maxPoints = request.maxPoints,
        )

        return DashboardChartsDto(
            speedTrend = speedTrend,
            congestion = congestion,
            yMax = request.yMax,
        )
    }

    private fun buildSpeedTrend(
        previousSpeedTrend: List<SpeedTrendPointDto>,
        currentTraffic: List<com.itd.traffic.dto.TrafficSegmentDto>,
        timeLabel: String,
        maxPoints: Int,
    ): List<SpeedTrendPointDto> {
        if (currentTraffic.isEmpty()) {
            return previousSpeedTrend.takeLast(maxPoints)
        }

        val avgSpeed = currentTraffic
            .map { it.currentFlow.speed.toDouble() }
            .average()

        return (previousSpeedTrend + SpeedTrendPointDto(
            label = timeLabel,
            avgSpeedKph = round1(avgSpeed),
        )).takeLast(maxPoints)
    }

    private fun buildCongestionBreakdown(
        jamFactors: List<Double>,
        goodThreshold: Double,
        moderateThreshold: Double,
    ): CongestionBreakdownDto {
        val good = jamFactors.count { it < goodThreshold }
        val moderate = jamFactors.count { it >= goodThreshold && it < moderateThreshold }
        val heavy = jamFactors.count { it >= moderateThreshold }

        return CongestionBreakdownDto(
            good = good,
            moderate = moderate,
            heavy = heavy,
        )
    }

    private fun round1(value: Double): Double {
        return ((value * 10).roundToInt()) / 10.0
    }
}
