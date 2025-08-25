package com.szyszkodar.szyszkomapka.presentation.mapScreen

import android.media.session.MediaSession.Token
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import org.maplibre.android.geometry.LatLng
import java.time.LocalDateTime

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val unapprovedBookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null,
    val errorShown: Boolean = false,
    val appMode: AppMode = AppMode.DEFAULT,
    val centerLatLng: LatLng = LatLng(0.0,0.0),
    val userLocation: LatLng? = null,
    var bearerToken: String? = null,
    val chosenBookpoint: BookpointUI? = null
) {
}
