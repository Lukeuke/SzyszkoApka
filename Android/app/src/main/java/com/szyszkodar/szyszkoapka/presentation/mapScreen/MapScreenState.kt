package com.szyszkodar.szyszkoapka.presentation.mapScreen

import com.szyszkodar.szyszkoapka.data.uiClasses.BookpointUI
import java.time.LocalDateTime

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null,
    val bookpointInfoVisible: Boolean = false,
    val chosenBookpoint: BookpointUI = BookpointUI("", 0.0, 0.0, LocalDateTime.now(), LocalDateTime.now(), "", false)
)
