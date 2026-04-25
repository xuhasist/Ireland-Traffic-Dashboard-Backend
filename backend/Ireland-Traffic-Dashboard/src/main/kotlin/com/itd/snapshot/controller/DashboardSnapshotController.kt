package com.itd.snapshot.controller

import com.itd.snapshot.dto.DashboardSnapshotHistoryListResponseDto
import com.itd.snapshot.dto.DashboardSnapshotListResponseDto
import com.itd.snapshot.dto.DashboardSnapshotResponseDto
import com.itd.snapshot.dto.DashboardSnapshotSaveRequestDto
import com.itd.snapshot.service.DashboardSnapshotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/dashboard-snapshots")
class DashboardSnapshotController(
    private val dashboardSnapshotService: DashboardSnapshotService,
) {

    @PostMapping
    fun saveSnapshot(
        @RequestBody request: DashboardSnapshotSaveRequestDto,
    ): DashboardSnapshotResponseDto {
        return dashboardSnapshotService.saveSnapshot(request)
    }

    @GetMapping("/latest")
    fun getLatestSnapshot(
        @RequestParam city: String,
    ): ResponseEntity<DashboardSnapshotResponseDto> {
        val latest = dashboardSnapshotService.getLatestSnapshot(city)
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(latest)
    }

    @GetMapping
    fun getRecentSnapshots(
        @RequestParam city: String,
        @RequestParam(defaultValue = "20") limit: Int,
    ): DashboardSnapshotListResponseDto {
        return dashboardSnapshotService.getRecentSnapshots(city, limit)
    }

    @GetMapping("/top10")
    fun getTopTenSnapshots(
    ): DashboardSnapshotHistoryListResponseDto {
        return dashboardSnapshotService.getTopTenSnapshots()
    }
}

