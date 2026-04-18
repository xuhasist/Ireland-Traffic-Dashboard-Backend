package com.itd.snapshot.dto

import com.itd.common.dto.ApiMeta

data class SnapshotTrendDto(
    val text: String,
    val dir: String,
)

data class SnapshotWeatherDto(
    val temperature: Double,
    val description: String,
    val icon: String,
    val dt: Long,
    val timezone: String,
)

data class SnapshotMetricsDto(
    val avgSpeed: Double?,
    val commuteTime: Double?,
    val congestedRoads: Int?,
    val activeIncidentsFiltered: Int?,
    val activeIncidentsTotal: Int?,
    val avgJam: Double?,
    val healthScore: Double?,
    val updatedAt: String?,
    val jamThreshold: Double,
    val trend: SnapshotTrendDto?,
)

data class SnapshotCongestionDto(
    val good: Int,
    val moderate: Int,
    val heavy: Int,
)

data class DashboardSnapshotSaveRequestDto(
    val city: String,
    val dataMode: String,
)

data class DashboardSnapshotItemDto(
    val id: String,
    val city: String,
    val dataMode: String,
    val trafficCount: Int,
    val incidentCount: Int,
    val generatedAt: String,
    val capturedAt: String,
    val weather: SnapshotWeatherDto?,
    val metrics: SnapshotMetricsDto?,
    val congestion: SnapshotCongestionDto,
)

data class DashboardSnapshotResponseDto(
    val meta: ApiMeta = ApiMeta(source = "mongodb"),
    val data: DashboardSnapshotItemDto,
)

data class DashboardSnapshotListResponseDto(
    val meta: ApiMeta = ApiMeta(source = "mongodb"),
    val data: List<DashboardSnapshotItemDto>,
)