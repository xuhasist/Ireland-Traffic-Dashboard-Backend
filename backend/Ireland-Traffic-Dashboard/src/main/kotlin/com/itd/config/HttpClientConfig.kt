package com.itd.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestClient
import java.time.Duration

@Configuration
class HttpClientConfig(
    private val externalApiProperties: ExternalApiProperties,
) {

    @Bean
    fun restClientBuilder(): RestClient.Builder {
        // add external API timeout settings
        val requestFactory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(Duration.ofSeconds(externalApiProperties.timeout.connectSeconds))
            setReadTimeout(Duration.ofSeconds(externalApiProperties.timeout.readSeconds))
        }

        return RestClient.builder()
            .requestFactory(requestFactory)
    }
}
