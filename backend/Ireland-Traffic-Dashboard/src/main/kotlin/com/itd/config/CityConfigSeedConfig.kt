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
            mapOf("name" to "Talbot Street", "lat" to 53.3504353, "lng" to -6.2563102),
            mapOf("name" to "Parnell Street", "lat" to 53.3501473, "lng" to -6.2670481),
            mapOf("name" to "College Green", "lat" to 53.3445818, "lng" to -6.2595687),
            mapOf("name" to "Westmoreland Street", "lat" to 53.3454921, "lng" to -6.259148),
            mapOf("name" to "Capel Street", "lat" to 53.3482491, "lng" to -6.2687204),
            mapOf("name" to "Jervis Street", "lat" to 53.3495105, "lng" to -6.2669459),
            mapOf("name" to "Parliament Street", "lat" to 53.3445312, "lng" to -6.2673698),
            mapOf("name" to "Pearse Street", "lat" to 53.344473, "lng" to -6.2511176),
            mapOf("name" to "Dorset Street", "lat" to 53.3535615, "lng" to -6.26836),
            mapOf("name" to "Baggot Street", "lat" to 53.3326025, "lng" to -6.243701),
            mapOf("name" to "Merrion Square", "lat" to 53.339152, "lng" to -6.2503308),
            mapOf("name" to "St Stephen's Green", "lat" to 53.3380517, "lng" to -6.2590232),
            mapOf("name" to "Thomas Street", "lat" to 53.3433655, "lng" to -6.2830178),
            mapOf("name" to "James's Street", "lat" to 53.3431108, "lng" to -6.2908996),
            mapOf("name" to "North Circular Road", "lat" to 53.3600583, "lng" to -6.263208),
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

