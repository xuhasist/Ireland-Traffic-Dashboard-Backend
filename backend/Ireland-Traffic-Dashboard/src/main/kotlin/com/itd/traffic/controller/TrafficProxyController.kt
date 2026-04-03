package com.itd.traffic.controller

import com.itd.traffic.dto.IncidentResponseDto
import com.itd.traffic.dto.TrafficFlowRequest
import com.itd.traffic.dto.TrafficFlowResponseDto
import com.itd.traffic.service.TomTomProxyService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/traffic")
class TrafficProxyController(
    private val tomTomProxyService: TomTomProxyService,
) {

    @PostMapping("/flow")
    fun getTrafficFlow(
        @RequestBody request: TrafficFlowRequest,
    ): TrafficFlowResponseDto {
        return tomTomProxyService.fetchTrafficFlow(request.roads)
    }

    @GetMapping("/incidents")
    fun getIncidents(
        @RequestParam minLon: Double,
        @RequestParam minLat: Double,
        @RequestParam maxLon: Double,
        @RequestParam maxLat: Double,
    ): IncidentResponseDto {
        return tomTomProxyService.fetchIncidents(
            minLon = minLon,
            minLat = minLat,
            maxLon = maxLon,
            maxLat = maxLat,
        )
    }
}

