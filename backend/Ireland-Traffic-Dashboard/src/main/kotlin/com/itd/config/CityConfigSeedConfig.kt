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

        val corkRoads = listOf(
            mapOf("name" to "St Patrick's Street", "lat" to 51.8983321, "lng" to -8.472843),
            mapOf("name" to "Grand Parade", "lat" to 51.8965952, "lng" to -8.4746255),
            mapOf("name" to "South Mall", "lat" to 51.8962452, "lng" to -8.4735832),
            mapOf("name" to "Oliver Plunkett Street", "lat" to 51.8975744, "lng" to -8.4725112),
            mapOf("name" to "Washington Street", "lat" to 51.897546, "lng" to -8.4792473),
            mapOf("name" to "Patrick's Quay", "lat" to 51.9003253, "lng" to -8.46299),
            mapOf("name" to "Anderson's Quay", "lat" to 51.8997441, "lng" to -8.4654267),
            mapOf("name" to "MacCurtain Street", "lat" to 51.9014929, "lng" to -8.468289),
            mapOf("name" to "North Main Street", "lat" to 51.8991007, "lng" to -8.4778687),
            mapOf("name" to "South Main Street", "lat" to 51.8958344, "lng" to -8.4762772),
            mapOf("name" to "Western Road", "lat" to 51.894201, "lng" to -8.4971505),
            mapOf("name" to "Sheares Street", "lat" to 51.8981759, "lng" to -8.4798997),
            mapOf("name" to "Lower Glanmire Road", "lat" to 51.9054278, "lng" to -8.4070232),
            mapOf("name" to "Douglas Road", "lat" to 51.8799283, "lng" to -8.4401663),
            mapOf("name" to "Blackrock Road", "lat" to 51.8970788, "lng" to -8.4162038),
            mapOf("name" to "Model Farm Road", "lat" to 51.8885654, "lng" to -8.5120059),
            mapOf("name" to "Bishopstown Road", "lat" to 51.8750631, "lng" to -8.5236275),
            mapOf("name" to "Wilton Road", "lat" to 51.8886193, "lng" to -8.5066859),
            mapOf("name" to "Carrigrohane Road", "lat" to 51.8933845, "lng" to -8.5113153),
            mapOf("name" to "Tivoli Road", "lat" to 51.9157267, "lng" to -8.4267312),
        )

        val galwayRoads = listOf(
            mapOf("name" to "Eyre Square", "lat" to 53.2743794, "lng" to -9.0492256),
            mapOf("name" to "Shop Street", "lat" to 53.2724335, "lng" to -9.0532516),
            mapOf("name" to "Quay Street", "lat" to 53.2710451, "lng" to -9.0541484),
            mapOf("name" to "Forster Street", "lat" to 53.2753644, "lng" to -9.0443073),
            mapOf("name" to "Eglinton Street", "lat" to 53.274442, "lng" to -9.0521013),
            mapOf("name" to "University Road", "lat" to 53.2759778, "lng" to -9.0594264),
            mapOf("name" to "Newcastle Road", "lat" to 53.2734339, "lng" to -9.0626023),
            mapOf("name" to "Headford Road", "lat" to 53.2825559, "lng" to -9.0475886),
            mapOf("name" to "Tuam Road", "lat" to 53.287639, "lng" to -9.0272775),
            mapOf("name" to "Dublin Road", "lat" to 53.2764659, "lng" to -9.0047147),
            mapOf("name" to "Seamus Quirke Road", "lat" to 53.2750111, "lng" to -9.077236),
            mapOf("name" to "Bohermore", "lat" to 53.2787266, "lng" to -9.0449172),
            mapOf("name" to "Wellpark Road", "lat" to 53.282532, "lng" to -9.0332125),
            mapOf("name" to "Lough Atalia Road", "lat" to 53.2729697, "lng" to -9.043575),
            mapOf("name" to "Dock Road", "lat" to 53.2707321, "lng" to -9.0508911),
            mapOf("name" to "Fr Griffin Road", "lat" to 53.2685738, "lng" to -9.0581751),
            mapOf("name" to "Upper Salthill Road", "lat" to 53.2628238, "lng" to -9.0726056),
            mapOf("name" to "Lower Salthill Road", "lat" to 53.267217, "lng" to -9.0681442),
            mapOf("name" to "The Promenade", "lat" to 53.2608756, "lng" to -9.0721628),
            mapOf("name" to "Bóthar na dTreabh", "lat" to 53.2889785, "lng" to -9.0103081),
        )

        val limerickRoads = listOf(
            mapOf("name" to "O'Connell Street", "lat" to 52.6624879, "lng" to -8.6281364),
            mapOf("name" to "William Street", "lat" to 52.6630664, "lng" to -8.6247461),
            mapOf("name" to "Patrick Street", "lat" to 52.6650432, "lng" to -8.6252641),
            mapOf("name" to "Henry Street", "lat" to 52.6595324, "lng" to -8.6331845),
            mapOf("name" to "Cecil Street", "lat" to 52.6612553, "lng" to -8.6274949),
            mapOf("name" to "Shannon Street", "lat" to 52.6626878, "lng" to -8.6289253),
            mapOf("name" to "Thomas Street", "lat" to 52.6626688, "lng" to -8.625923),
            mapOf("name" to "Mulgrave Street", "lat" to 52.6596108, "lng" to -8.616968),
            mapOf("name" to "Roxboro Road", "lat" to 52.6563888, "lng" to -8.6180489),
            mapOf("name" to "Dublin Road", "lat" to 52.6636786, "lng" to -8.5969311),
            mapOf("name" to "Ennis Road", "lat" to 52.6729139, "lng" to -8.6691575),
            mapOf("name" to "Dock Road", "lat" to 52.646592, "lng" to -8.6693603),
            mapOf("name" to "Childers Road", "lat" to 52.6481385, "lng" to -8.6322412),
            mapOf("name" to "Ballinacurra Road", "lat" to 52.6439396, "lng" to -8.6460509),
            mapOf("name" to "South Circular Road", "lat" to 52.6524575, "lng" to -8.6390632),
            mapOf("name" to "Clare Street", "lat" to 52.6655905, "lng" to -8.6125188),
            mapOf("name" to "Parnell Street", "lat" to 52.6597442, "lng" to -8.6250059),
            mapOf("name" to "Barrington Street", "lat" to 52.6583164, "lng" to -8.6313605),
            mapOf("name" to "Newenham Street", "lat" to 52.6587455, "lng" to -8.6330813),
            mapOf("name" to "Catherine Street", "lat" to 52.661149, "lng" to -8.627609),
        )

        val waterfordRoads = listOf(
            mapOf("name" to "The Quay", "lat" to 52.2393483, "lng" to -6.9724471),
            mapOf("name" to "Merchant's Quay", "lat" to 52.2637538, "lng" to -7.1181641),
            mapOf("name" to "Parade Quay", "lat" to 52.260621, "lng" to -7.1053366),
            mapOf("name" to "O'Connell Street", "lat" to 52.2618159, "lng" to -7.1137506),
            mapOf("name" to "Patrick Street", "lat" to 52.2600379, "lng" to -7.1122554),
            mapOf("name" to "John Street", "lat" to 52.2576216, "lng" to -7.1116742),
            mapOf("name" to "Michael Street", "lat" to 52.2588546, "lng" to -7.1118775),
            mapOf("name" to "Barronstrand Street", "lat" to 52.2616392, "lng" to -7.1116086),
            mapOf("name" to "High Street", "lat" to 52.2607268, "lng" to -7.1098613),
            mapOf("name" to "Catherine Street", "lat" to 52.2576502, "lng" to -7.1072702),
            mapOf("name" to "Bridge Street", "lat" to 52.2633838, "lng" to -7.1193588),
            mapOf("name" to "The Mall", "lat" to 52.2598087, "lng" to -7.1060535),
            mapOf("name" to "Dunmore Road", "lat" to 52.2469433, "lng" to -7.0799638),
            mapOf("name" to "Cork Road", "lat" to 52.2450579, "lng" to -7.1355153),
            mapOf("name" to "Dublin Road", "lat" to 52.1546054, "lng" to -8.2781502),
            mapOf("name" to "Tramore Road", "lat" to 52.2471469, "lng" to -7.1189771),
            mapOf("name" to "Ballybricken", "lat" to 52.2603785, "lng" to -7.1205244),
            mapOf("name" to "Manor Street", "lat" to 52.2543912, "lng" to -7.1142211),
            mapOf("name" to "New Street", "lat" to 52.2583185, "lng" to -7.1125192),
            mapOf("name" to "Poleberry", "lat" to 52.2517856, "lng" to -7.110356),
        )

        cityConfigRepository.saveAll(
            listOf(
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
                ),
                CityConfigEntity(
                    cityName = "Cork",
                    centerLat = 51.8985136,
                    centerLng = -8.4726423,
                    bboxMinLon = -8.6378543,
                    bboxMinLat = 51.8273102,
                    bboxMaxLon = -8.3551315,
                    bboxMaxLat = 51.9701415,
                    roadsJson = mapper.writeValueAsString(corkRoads),
                    enabled = true,
                ),
                CityConfigEntity(
                    cityName = "Galway",
                    centerLat = 53.2744122,
                    centerLng = -9.0490601,
                    bboxMinLon = -9.1426901,
                    bboxMinLat = 53.2485189,
                    bboxMaxLon = -8.9548381,
                    bboxMaxLat = 53.3197423,
                    roadsJson = mapper.writeValueAsString(galwayRoads),
                    enabled = true,
                ),
                CityConfigEntity(
                    cityName = "Limerick",
                    centerLat = 52.661252,
                    centerLng = -8.6301239,
                    bboxMinLon = -8.8070765,
                    bboxMinLat = 52.5721036,
                    bboxMaxLon = -8.4425444,
                    bboxMaxLat = 52.757379,
                    roadsJson = mapper.writeValueAsString(limerickRoads),
                    enabled = true,
                ),
                CityConfigEntity(
                    cityName = "Waterford",
                    centerLat = 52.2609997,
                    centerLng = -7.1119081,
                    bboxMinLon = -7.1869522,
                    bboxMinLat = 52.2102427,
                    bboxMaxLon = -7.0338797,
                    bboxMaxLat = 52.2798229,
                    roadsJson = mapper.writeValueAsString(waterfordRoads),
                    enabled = true,
                ),
            )
        )
    }
}

