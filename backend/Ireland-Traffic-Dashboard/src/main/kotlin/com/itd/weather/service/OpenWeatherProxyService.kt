package com.itd.weather.service

import com.fasterxml.jackson.databind.JsonNode
import com.itd.weather.dto.WeatherResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.util.UriComponentsBuilder

@Service
class OpenWeatherProxyService(
    private val restClientBuilder: RestClient.Builder,
    @Value("\${external.openweather.api-key}") private val apiKey: String,
    @Value("\${external.openweather.base-url}") private val baseUrl: String,
) {
    private val restClient: RestClient = restClientBuilder.build()

    @Cacheable(
        cacheNames = ["weather"],
        key = "T(String).format(T(java.util.Locale).US,'%.4f:%.4f',#lat,#lon)"
    )
    fun fetchWeather(lat: Double, lon: Double): WeatherResponseDto? {
        if (apiKey.isBlank()) return null

        // const url = `${this.BASE_URL}?lat=${lat}&lon=${lon}&units=metric&appid=${this.API_KEY}`;
        val url = UriComponentsBuilder
            .fromHttpUrl(baseUrl)
            .queryParam("lat", lat)
            .queryParam("lon", lon)
            .queryParam("units", "metric")
            .queryParam("appid", apiKey)
            .build(true)
            .toUriString()

        val root = restClient.get()
            .uri(url)
            .retrieve()
            .body(JsonNode::class.java)
            ?: return null

        return WeatherResponseDto(
            temperature = root.path("main").path("temp").asDouble(),
            description = root.path("weather").firstOrNull()?.path("description")?.asText("Unknown")
                ?: "Unknown",
            icon = root.path("weather").firstOrNull()?.path("icon")?.asText("01d")
                ?: "01d",
            dt = root.path("dt").asLong(0),
            timezone = root.path("timezone").asText("0"),
        )
    }
}

