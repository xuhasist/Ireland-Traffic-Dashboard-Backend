package com.itd.location.controller

import com.itd.location.dto.CityConfigListResponseDto
import com.itd.location.service.CityConfigService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/city-configs")
class CityConfigController(
    private val cityConfigService: CityConfigService,
) {
    @GetMapping
    fun getCityConfigs(): CityConfigListResponseDto {
        return cityConfigService.getEnabledCityConfigs()
    }
}

