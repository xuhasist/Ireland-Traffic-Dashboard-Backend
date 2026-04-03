package com.itd.traffic.support

import com.itd.traffic.dto.RoadPointRequest

object CacheKeyFactory {

    @JvmStatic
    fun bboxKey(
        minLon: Double,
        minLat: Double,
        maxLon: Double,
        maxLat: Double,
    ): String {
        return listOf(minLon, minLat, maxLon, maxLat).joinToString(",")
    }

    @JvmStatic
    fun roadsKey(roads: List<RoadPointRequest>): String {
        return roads
            .map { "${it.name}:${it.lat},${it.lng}" }
            .sorted()
            .joinToString("|")
    }
}