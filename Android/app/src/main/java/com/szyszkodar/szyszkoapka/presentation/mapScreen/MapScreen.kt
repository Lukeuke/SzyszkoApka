package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkoapka.presentation.bookpointInfoPopUp.BookpointInfoPopUp
import com.szyszkodar.szyszkoapka.presentation.mapScreen.components.MapLibreView

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val viewmodel = hiltViewModel<MapScreenViewModel>()
    val state = viewmodel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Box(contentAlignment = Alignment.Center) {
        MapLibreView(context, viewmodel)
        AnimatedVisibility(state.value.bookpointInfoVisible) {
            BookpointInfoPopUp(state.value.chosenBookpoint)
        }
    }

    // Show error message on error
    state.value.errorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
    }
}