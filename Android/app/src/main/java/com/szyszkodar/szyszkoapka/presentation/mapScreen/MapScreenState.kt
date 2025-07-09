package com.szyszkodar.szyszkoapka.presentation.mapScreen

import com.szyszkodar.szyszkoapka.data.uiClasses.BookpointUI

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null
)
