package com.itd.dashboard.dto

import com.itd.common.dto.ApiMeta
import com.itd.traffic.dto.TrafficSegmentDto

data class SpeedTrendPointDto(
    val label: String,
    val avgSpeedKph: Double,
)

data class CongestionBreakdownDto(
    val good: Int,
    val moderate: Int,
    val heavy: Int,
)

data class DashboardChartsDto(
    val speedTrend: List<SpeedTrendPointDto>,
    val congestion: CongestionBreakdownDto,
    val yMax: Int,
)

data class DashboardChartsRequestDto(
    val traffic: List<TrafficSegmentDto>,
    val previousSpeedTrend: List<SpeedTrendPointDto>,
    val timeLabel: String,
    val goodThreshold: Double,
    val moderateThreshold: Double,
    val maxPoints: Int,
    val yMax: Int,
)

data class DashboardChartsResponseDto(
    val meta: ApiMeta = ApiMeta(),
    val data: DashboardChartsDto,
)
