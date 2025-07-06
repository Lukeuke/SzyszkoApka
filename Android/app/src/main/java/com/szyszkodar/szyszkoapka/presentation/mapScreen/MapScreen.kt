package com.szyszkodar.szyszkoapka.presentation.mapScreen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.szyszkodar.szyszkoapka.presentation.MapLibreView
import org.maplibre.android.maps.MapView

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val viewmodel = hiltViewModel<MapScreenViewModel>()
    val context = LocalContext.current

    MapLibreView(context)
}