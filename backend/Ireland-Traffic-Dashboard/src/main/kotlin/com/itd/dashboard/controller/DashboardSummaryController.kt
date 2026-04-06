package com.itd.dashboard.controller

import com.itd.dashboard.dto.DashboardSummaryRequestDto
import com.itd.dashboard.dto.DashboardSummaryResponseDto
import com.itd.dashboard.service.DashboardSummaryService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/dashboard")
class DashboardSummaryController(
    private val dashboardSummaryService: DashboardSummaryService,
) {

    @PostMapping("/summary")
    fun getSummary(
        @RequestBody request: DashboardSummaryRequestDto,
    ): DashboardSummaryResponseDto {
        return DashboardSummaryResponseDto(
            data = dashboardSummaryService.buildSummary(request),
        )
    }
}

