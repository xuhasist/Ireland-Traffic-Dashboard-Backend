package com.itd.weather.controller

import com.itd.weather.dto.WeatherResponseDto
import com.itd.weather.service.OpenWeatherProxyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/weather")
class WeatherController(
    private val openWeatherProxyService: OpenWeatherProxyService,
) {

    @GetMapping
    fun getWeather(
        @RequestParam lat: Double,
        @RequestParam lng: Double,
    ): WeatherResponseDto? {
        return openWeatherProxyService.fetchWeather(lat, lng)
    }
}

