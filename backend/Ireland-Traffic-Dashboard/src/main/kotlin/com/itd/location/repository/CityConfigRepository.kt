package com.itd.location.repository

import com.itd.location.entity.CityConfigEntity
import org.springframework.data.jpa.repository.JpaRepository

interface CityConfigRepository : JpaRepository<CityConfigEntity, Long> {
    fun findAllByEnabledTrueOrderByCityNameAsc(): List<CityConfigEntity>
    fun findByCityNameIgnoreCaseAndEnabledTrue(cityName: String): CityConfigEntity?
}

