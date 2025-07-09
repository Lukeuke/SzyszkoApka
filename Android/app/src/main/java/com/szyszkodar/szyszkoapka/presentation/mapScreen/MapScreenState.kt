package com.szyszkodar.szyszkoapka.presentation.mapScreen

import androidx.core.app.NotificationCompat.MessagingStyle.Message
import com.szyszkodar.szyszkoapka.data.uiClasses.BookpointUI
import org.maplibre.android.maps.MapView

data class MapScreenState(
    val bookpoints: List<BookpointUI> = emptyList(),
    val errorMessage: String? = null
)
