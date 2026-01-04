package com.szyszkodar.szyszkomapka.data.uiClasses

import com.szyszkodar.szyszkomapka.domain.uiClasses.UIClass
import java.time.LocalDateTime

class BookpointUI(
    val id: String,
    val latitude: Double,
    val longitude: Double,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val title: String,
    val description: String,
    val images: List<String>?,
    val approved: Boolean
): UIClass
