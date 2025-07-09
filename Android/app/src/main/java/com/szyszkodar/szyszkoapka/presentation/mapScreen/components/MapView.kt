package com.szyszkodar.szyszkoapka.presentation.mapScreen.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkoapka.presentation.mapScreen.MapScreenViewModel
import org.maplibre.android.maps.MapView

@Composable
fun MapLibreView (
    context: Context,
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }

    AndroidView(
        modifier = modifier,
        factory = {
            val mapView = MapView(context)
            mapViewRef.value = mapView

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
}