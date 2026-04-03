package com.itd.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.itd.location.entity.CityConfigEntity
import com.itd.location.repository.CityConfigRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CityConfigSeedConfig {

    @Bean
    fun seedCityConfigs(cityConfigRepository: CityConfigRepository) = CommandLineRunner {
        if (cityConfigRepository.count() > 0) {
            return@CommandLineRunner
        }

        val mapper = jacksonObjectMapper()

        val dublinRoads = listOf(
            mapOf("name" to "O'Connell Street", "lat" to 53.3509547, "lng" to -6.2605881),
            mapOf("name" to "Grafton Street", "lat" to 53.3420874, "lng" to -6.2598865),
            mapOf("name" to "Dame Street", "lat" to 53.3441751, "lng" to -6.2646484),
            mapOf("name" to "Nassau Street", "lat" to 53.3432662, "lng" to -6.2592181),
            mapOf("name" to "Abbey Street", "lat" to 53.3485609, "lng" to -6.2581508),
        )

        cityConfigRepository.save(
            CityConfigEntity(
                cityName = "Dublin",
                centerLat = 53.3493795,
                centerLng = -6.2605593,
                bboxMinLon = -6.3870259,
                bboxMinLat = 53.2987342,
                bboxMaxLon = -6.1148829,
                bboxMaxLat = 53.4105416,
                roadsJson = mapper.writeValueAsString(dublinRoads),
                enabled = true,
            )
        )
    }
}

