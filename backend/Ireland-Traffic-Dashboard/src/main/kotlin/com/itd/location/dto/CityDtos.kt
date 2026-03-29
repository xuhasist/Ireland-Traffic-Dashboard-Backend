package com.itd.location.dto

import com.itd.common.dto.ApiMeta


data class CityResponseDto(
    val id: Long,
    val name: String,
    val countryCode: String,
    val lat: Double,
    val lng: Double,
)

data class CityListResponseDto(
    val meta: ApiMeta = ApiMeta(),
    val data: List<CityResponseDto>
)
