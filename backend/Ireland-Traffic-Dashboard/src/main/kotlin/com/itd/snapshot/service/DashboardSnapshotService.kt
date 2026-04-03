package com.itd.snapshot.service

import com.itd.snapshot.document.DashboardSnapshotDocument
import com.itd.snapshot.dto.*
import com.itd.snapshot.repository.DashboardSnapshotRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class DashboardSnapshotService(
    private val dashboardSnapshotRepository: DashboardSnapshotRepository,
) {

    fun saveSnapshot(request: DashboardSnapshotSaveRequestDto): DashboardSnapshotResponseDto {
        val saved = dashboardSnapshotRepository.save(
            DashboardSnapshotDocument(
                city = request.city,
                dataMode = request.dataMode,
                trafficCount = request.trafficCount,
                incidentCount = request.incidentCount,
                generatedAt = parseInstantOrNow(request.generatedAt),
                weather = request.weather,
                metrics = request.metrics,
                congestion = request.congestion,
            )
        )

        return DashboardSnapshotResponseDto(
            data = saved.toItemDto()
        )
    }

    fun getLatestSnapshot(city: String): DashboardSnapshotResponseDto? {
        val latest = dashboardSnapshotRepository.findTopByCityOrderByCapturedAtDesc(city)
            ?: return null

        return DashboardSnapshotResponseDto(
            data = latest.toItemDto()
        )
    }

    fun getRecentSnapshots(city: String, limit: Int): DashboardSnapshotListResponseDto {
        val safeLimit = limit.coerceIn(1, 100)

        val items = dashboardSnapshotRepository.findByCityOrderByCapturedAtDesc(
            city,
            PageRequest.of(0, safeLimit),
        )

        return DashboardSnapshotListResponseDto(
            data = items.map { it.toItemDto() }
        )
    }

    private fun parseInstantOrNow(value: String): Instant {
        return try {
            Instant.parse(value)
        } catch (_: Exception) {
            Instant.now()
        }
    }

    private fun DashboardSnapshotDocument.toItemDto(): DashboardSnapshotItemDto {
        return DashboardSnapshotItemDto(
            id = this.id ?: "",
            city = this.city,
            dataMode = this.dataMode,
            trafficCount = this.trafficCount,
            incidentCount = this.incidentCount,
            generatedAt = this.generatedAt.toString(),
            capturedAt = this.capturedAt.toString(),
            weather = this.weather,
            metrics = this.metrics,
            congestion = this.congestion,
        )
    }
}

