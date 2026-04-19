package com.itd.dashboard.dto

import com.itd.common.dto.ApiMeta
import com.itd.traffic.dto.IncidentDto
import com.itd.traffic.dto.TrafficSegmentDto
import com.itd.weather.dto.WeatherResponseDto

data class DashboardAggregateResponseDto(
    val meta: ApiMeta = ApiMeta(),
    val city: String,
    val mode: String,
    val weather: WeatherResponseDto?,
    val traffic: List<TrafficSegmentDto>,
    val incidents: List<IncidentDto>,
    val metrics: DashboardSummaryDto,
    val charts: DashboardChartsDto,
)

