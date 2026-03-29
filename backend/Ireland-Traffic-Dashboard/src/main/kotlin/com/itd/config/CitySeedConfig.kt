package com.itd.config

import com.itd.location.entity.City
import com.itd.location.repository.CityRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CitySeedConfig {
    @Bean
    fun seedCities(cityRepository: CityRepository): CommandLineRunner {
        return CommandLineRunner {
            if (cityRepository.count() > 0) {   // If there are already cities in the database, skip seeding
                return@CommandLineRunner
            }

            cityRepository.saveAll(
                listOf(
                    City(
                        name = "Dublin",
                        countryCode = "IE",
                        latitude = 53.3493795,
                        longitude = -6.2605593,
                        displayOrder = 1,
                    ),
                    City(
                        name = "Cork",
                        countryCode = "IE",
                        latitude = 51.8985136,
                        longitude = -8.4726423,
                        displayOrder = 2,
                    ),
                    City(
                        name = "Galway",
                        countryCode = "IE",
                        latitude = 53.2720928,
                        longitude = -9.0480153,
                        displayOrder = 3,
                    ),
                    City(
                        name = "Limerick",
                        countryCode = "IE",
                        latitude = 52.661252,
                        longitude = -8.6301239,
                        displayOrder = 4,
                    ),
                    City(
                        name = "Waterford",
                        countryCode = "IE",
                        latitude = 52.2609997,
                        longitude = -7.1119081,
                        displayOrder = 5,
                    ),
                )
            )
        }
    }
}
