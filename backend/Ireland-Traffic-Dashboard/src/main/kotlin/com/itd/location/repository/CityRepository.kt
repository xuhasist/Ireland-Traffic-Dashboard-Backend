package com.itd.location.repository

import com.itd.location.entity.City
import org.springframework.data.jpa.repository.JpaRepository

interface CityRepository : JpaRepository<City, Long> {
    fun findAllByEnabledTrueOrderByDisplayOrderAscNameAsc(): List<City>
    fun existsByName(name: String): Boolean
}
