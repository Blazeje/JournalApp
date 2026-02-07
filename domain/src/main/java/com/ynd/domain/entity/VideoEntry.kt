package com.ynd.domain.entity

import java.util.*

data class VideoEntry(
    val id: String = UUID.randomUUID().toString(),
    val fileUri: String,
    val description: String?,
    val createdAt: Long = System.currentTimeMillis()
)
