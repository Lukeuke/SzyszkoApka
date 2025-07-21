package com.szyszkodar.szyszkomapka.presentation.mapScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoPopUp.BookpointInfoPopUp
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.MapLibreView
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }
    val viewmodel = hiltViewModel<MapScreenViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionHandler = LocalizationHandler(context)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        permissionHandler.ShowDialog(
            onGranted = {
                mapViewRef.value?.let { viewmodel.onLocalizationPermitted(it) }
            }
        )

        MapLibreView(context, viewmodel, mapViewRef)
        AnimatedVisibility(state.value.bookpointInfoVisible) {
            BookpointInfoPopUp(state.value.chosenBookpoint)
        }
    }

    // Show error message on error
    state.value.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}