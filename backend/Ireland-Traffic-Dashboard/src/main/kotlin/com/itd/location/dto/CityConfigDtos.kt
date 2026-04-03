package com.itd.location.dto

import com.itd.common.dto.ApiMeta

data class RoadPointDto(
    val name: String,
    val lat: Double,
    val lng: Double,
)

data class BBoxDto(
    val minLon: Double,
    val minLat: Double,
    val maxLon: Double,
    val maxLat: Double,
)

data class CityConfigItemDto(
    val cityName: String,
    val center: List<Double>,
    val bbox: BBoxDto,
    val roads: List<RoadPointDto>,
)

data class CityConfigListResponseDto(
    val meta: ApiMeta = ApiMeta(),
    val data: List<CityConfigItemDto>,
)

