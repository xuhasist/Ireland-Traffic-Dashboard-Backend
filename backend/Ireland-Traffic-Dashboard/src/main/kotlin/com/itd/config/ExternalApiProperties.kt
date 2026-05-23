package com.itd.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "external-api")
data class ExternalApiProperties(
    val timeout: Timeout = Timeout(),
    val tomtom: ApiProvider = ApiProvider(
        baseUrl = "https://api.tomtom.com/traffic/services",    // if cannot find setting in application.properties, use this default value
    ),
    val openweather: ApiProvider = ApiProvider(
        baseUrl = "https://api.openweathermap.org/data/2.5/weather",
    ),
) {
    data class Timeout(
        val connectSeconds: Long = 3,
        val readSeconds: Long = 5,
    )

    data class ApiProvider(
        val apiKey: String = "",
        val baseUrl: String = "",
    )
}
