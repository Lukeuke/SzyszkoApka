package com.szyszkodar.szyszkomapka.data.intentsHandling

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.intentsHandling.IntentHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import org.maplibre.android.geometry.LatLng

class GoogleMapsOpener(
    @ApplicationContext context: Context
): IntentHandler(
    context = context
) {
    fun openMapsRoute(latLng: LatLng): String? {
        val uri = "https://www.google.com/maps/dir/?api=1&destination=${latLng.latitude},${latLng.longitude}&travelmode=driving".toUri()
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        return when(val result = openIntent(intent)) {
            is Result.Success -> null
            is Result.Error -> {
                val webOpener = WebOpener(context)
                webOpener.openWebPage(uri) ?: result.error.message
            }
        }
    }
}