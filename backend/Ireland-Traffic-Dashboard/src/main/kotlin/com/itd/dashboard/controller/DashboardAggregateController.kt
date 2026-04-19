package com.itd.dashboard.controller

import com.itd.dashboard.dto.DashboardAggregateResponseDto
import com.itd.dashboard.service.DashboardAggregateService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardAggregateController(
    private val dashboardAggregateService: DashboardAggregateService,
) {

    @GetMapping
    fun getDashboard(
        @RequestParam city: String,
        @RequestParam(defaultValue = "live") mode: String,
    ): DashboardAggregateResponseDto {
        return dashboardAggregateService.getDashboard(city = city, mode = mode)
    }
}

