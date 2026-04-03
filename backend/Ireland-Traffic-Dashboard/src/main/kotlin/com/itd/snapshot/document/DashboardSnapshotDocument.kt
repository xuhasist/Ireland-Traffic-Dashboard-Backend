package com.itd.snapshot.document

import com.itd.snapshot.dto.SnapshotCongestionDto
import com.itd.snapshot.dto.SnapshotMetricsDto
import com.itd.snapshot.dto.SnapshotWeatherDto
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.CompoundIndex
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "dashboard_snapshots")
@CompoundIndex(name = "city_capturedAt_idx", def = "{'city': 1, 'capturedAt': -1}")
data class DashboardSnapshotDocument(
    @Id
    val id: String? = null,
    val city: String,
    val dataMode: String,
    val trafficCount: Int,
    val incidentCount: Int,
    val generatedAt: Instant,
    val capturedAt: Instant = Instant.now(),
    val weather: SnapshotWeatherDto?,
    val metrics: SnapshotMetricsDto?,
    val congestion: SnapshotCongestionDto,
)

