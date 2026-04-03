package com.itd.snapshot.repository

import com.itd.snapshot.document.DashboardSnapshotDocument
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface DashboardSnapshotRepository : MongoRepository<DashboardSnapshotDocument, String> {
    fun findTopByCityOrderByCapturedAtDesc(city: String): DashboardSnapshotDocument?
    fun findByCityOrderByCapturedAtDesc(city: String, pageable: Pageable): List<DashboardSnapshotDocument>
}

