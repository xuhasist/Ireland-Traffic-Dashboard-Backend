package com.itd.location.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "city_configs")
class CityConfigEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "city_name", nullable = false, unique = true, length = 100)
    val cityName: String,

    @Column(name = "center_lat", nullable = false)
    val centerLat: Double,

    @Column(name = "center_lng", nullable = false)
    val centerLng: Double,

    @Column(name = "bbox_min_lon", nullable = false)
    val bboxMinLon: Double,

    @Column(name = "bbox_min_lat", nullable = false)
    val bboxMinLat: Double,

    @Column(name = "bbox_max_lon", nullable = false)
    val bboxMaxLon: Double,

    @Column(name = "bbox_max_lat", nullable = false)
    val bboxMaxLat: Double,

    @Column(name = "roads_json", nullable = false, columnDefinition = "TEXT")
    val roadsJson: String,

    @Column(nullable = false)
    val enabled: Boolean = true,
)

