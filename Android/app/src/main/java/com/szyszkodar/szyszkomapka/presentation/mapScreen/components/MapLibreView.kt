package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import android.content.Context
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import org.maplibre.android.maps.MapView

@Composable
fun MapLibreView (
    context: Context,
    viewModel: MapScreenViewModel,
    mapViewRef: MutableState<MapView?>,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val localDensity = LocalDensity.current
    val topPadding = with(localDensity) { paddingValues.calculateTopPadding().toPx().toInt() }

    AndroidView(
        modifier = modifier,
        factory = {
            val mapView = MapView(context)

            mapViewRef.value = mapView
            viewModel.setCompassMargins(
                mapView = mapView,
                topPadding = topPadding
            )
            viewModel.createMap(mapView)
        },
        update = {
            mapViewRef.value = it
            viewModel.updateMap(it)
        }
    )

    LaunchedEffect(state.value.bookpoints) {
        mapViewRef.value?.let {
            viewModel.updateMap(it)
        }
    }

    LaunchedEffect(state.value.appMode) {
        mapViewRef.value?.let {
            viewModel.updateMap(it)
        }
    }
}