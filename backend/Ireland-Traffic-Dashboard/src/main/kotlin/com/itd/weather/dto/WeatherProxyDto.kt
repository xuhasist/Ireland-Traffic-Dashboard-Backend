package com.itd.weather.dto

data class WeatherResponseDto(
    val temperature: Double,
    val description: String,
    val icon: String,
    val dt: Long,
    val timezone: String,
)

