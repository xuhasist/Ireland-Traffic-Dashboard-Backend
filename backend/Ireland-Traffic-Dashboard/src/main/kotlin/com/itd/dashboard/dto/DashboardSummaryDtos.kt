package com.itd.dashboard.dto

import com.itd.common.dto.ApiMeta
import com.itd.traffic.dto.IncidentDto
import com.itd.traffic.dto.TrafficSegmentDto

data class MetricTrendDto(
    val text: String,
    val dir: String, // "up" | "down"
)

data class DashboardSummaryDto(
    val avgSpeedKph: Double?,
    val commuteTimeMinutes: Int?,
    val congestedRoadCount: Int?,
    val activeIncidentCount: Int?,
    val avgJamFactor: Double?,
    val healthScore: Int?,
    val jamThreshold: Double,
    val updatedAt: String?,
    val trend: MetricTrendDto?,
)

data class DashboardSummaryResponseDto(
    val meta: ApiMeta = ApiMeta(),
    val data: DashboardSummaryDto,
)

data class DashboardSummaryRequestDto(
    val traffic: List<TrafficSegmentDto>,
    val incidents: List<IncidentDto>,
    val filteredIncidentCount: Int,
    val updatedAt: String,
    val previousAvgSpeed: Double?,
    val jamThreshold: Double,
)

