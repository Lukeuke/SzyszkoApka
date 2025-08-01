package com.szyszkodar.szyszkomapka.presentation.mapScreen

import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import org.maplibre.android.geometry.LatLng
import java.time.LocalDateTime

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null,
    val errorShown: Boolean = false,
    val appMode: AppMode = AppMode.DEFAULT,
    val bookpointInfoVisible: Boolean = false,
    val userLocation: LatLng? = null,
    val chosenBookpoint: BookpointUI = BookpointUI("", 0.0, 0.0, LocalDateTime.now(), LocalDateTime.now(), "", false)
)
