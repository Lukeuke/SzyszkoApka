package com.szyszkodar.szyszkoapka.data.uiClasses

import com.szyszkodar.szyszkoapka.domain.uiClasses.UIClass
import java.time.LocalDateTime

data class BookpointUI(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val description: String,
    val approved: Boolean
): UIClass
