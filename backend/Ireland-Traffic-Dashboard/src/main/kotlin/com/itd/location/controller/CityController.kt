package com.itd.location.controller

import com.itd.location.dto.CityListResponseDto
import com.itd.location.service.CityService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cities")
class CityController(
    private val cityService: CityService,
) {
    @GetMapping
    fun getCities(): CityListResponseDto {
        return CityListResponseDto(data = cityService.getEnabledCities())
    }
}
