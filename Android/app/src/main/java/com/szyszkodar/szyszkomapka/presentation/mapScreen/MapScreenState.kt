package com.szyszkodar.szyszkomapka.presentation.mapScreen

import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import org.maplibre.android.geometry.LatLng

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val unapprovedBookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null,
    val errorShown: Boolean = false,
    val appMode: AppMode = AppMode.DEFAULT,
    val centerLatLng: LatLng = LatLng(0.0,0.0),
    val userLocation: LatLng? = null,
    val chosenBookpoint: BookpointUI? = null
)
