package com.szyszkodar.szyszkoapka.data.uiClasses

import com.szyszkodar.szyszkoapka.domain.uiClasses.UIClass
import java.time.LocalDateTime

data class BookpointsUI(
    val id: String,
    val latitude: Long,
    val longitude: Long,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
): UIClass
