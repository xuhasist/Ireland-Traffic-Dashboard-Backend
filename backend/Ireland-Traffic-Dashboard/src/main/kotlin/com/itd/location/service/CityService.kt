package com.itd.location.service

import com.itd.location.dto.CityResponseDto
import com.itd.location.repository.CityRepository
import org.springframework.stereotype.Service

@Service
class CityService(
    private val cityRepository: CityRepository,
) {
    fun getEnabledCities(): List<CityResponseDto> {
        return cityRepository.findAllByEnabledTrueOrderByDisplayOrderAscNameAsc()
            .map {
                CityResponseDto(
                    id = it.id,
                    name = it.name,
                    countryCode = it.countryCode,
                    lat = it.latitude,
                    lng = it.longitude,
                )
            }
    }
}
