package com.itd.dashboard.controller

import com.itd.dashboard.dto.DashboardChartsRequestDto
import com.itd.dashboard.dto.DashboardChartsResponseDto
import com.itd.dashboard.service.DashboardChartsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardChartsController(
    private val dashboardChartsService: DashboardChartsService,
) {

    @PostMapping("/charts")
    fun getCharts(
        @RequestBody request: DashboardChartsRequestDto,
    ): DashboardChartsResponseDto {
        return DashboardChartsResponseDto(
            data = dashboardChartsService.buildCharts(request),
        )
    }
}
