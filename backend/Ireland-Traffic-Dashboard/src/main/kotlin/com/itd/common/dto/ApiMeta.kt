package com.itd.common.dto

import java.time.OffsetDateTime


data class ApiMeta(
    val timestamp: String = OffsetDateTime.now().toString(),
    val source: String = "backend"
)
