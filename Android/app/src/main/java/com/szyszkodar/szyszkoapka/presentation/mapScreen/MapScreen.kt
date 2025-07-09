package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkoapka.presentation.mapScreen.components.MapLibreView

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val viewmodel = hiltViewModel<MapScreenViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    Column {
        MapLibreView(context, viewmodel)
        Log.d("MAPXDD", state.value.toString())
    }
}