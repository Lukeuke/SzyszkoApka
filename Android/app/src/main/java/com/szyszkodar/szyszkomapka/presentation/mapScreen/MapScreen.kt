package com.szyszkodar.szyszkomapka.presentation.mapScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.MapLibreView
import com.szyszkodar.szyszkomapka.presentation.mapScreen.modes.AddBookpointMode
import com.szyszkodar.szyszkomapka.presentation.mapScreen.modes.AdminMode
import com.szyszkodar.szyszkomapka.presentation.mapScreen.modes.DefaultMode
import org.maplibre.android.maps.MapView

@Composable
fun MapScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val mapViewRef = remember { mutableStateOf<MapView?>(null) }
    val viewModel = hiltViewModel<MapScreenViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val permissionHandler = LocalizationHandler(context)

    LaunchedEffect(state.value.userLocation) {
        mapViewRef.value?.let { viewModel.updateMap(it) }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
    ) {
        permissionHandler.ShowDialog(
            onGranted = {
                mapViewRef.value?.let {
                    viewModel.onLocalizationPermitted(it)
                }
            }
        )

        MapLibreView(context, viewModel, mapViewRef, paddingValues)

        AnimatedContent(
            targetState = state.value.appMode,
            transitionSpec = {
                when(state.value.appMode) {
                    AppMode.DEFAULT -> slideInHorizontally{it} togetherWith slideOutHorizontally{-it}
                    AppMode.ADMIN -> slideInHorizontally{it} togetherWith slideOutHorizontally{-it}
                    AppMode.ADD_BOOKPOINT -> slideInHorizontally{-it} togetherWith slideOutHorizontally{it}
                }
            }
        ) { mode ->
            when(mode) {
                AppMode.DEFAULT -> DefaultMode(
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
                AppMode.ADMIN -> AdminMode(
                    paddingValues = paddingValues,
                    viewModel = viewModel
                )
                AppMode.ADD_BOOKPOINT -> mapViewRef.value?.let {
                    AddBookpointMode(
                        paddingValues = paddingValues,
                        viewModel = viewModel,
                        mapView = it
                    )
                }
            }

        }

    }


    // Show error message on error
    state.value.errorMessage?.let {
        if(!state.value.errorShown) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()

            // Show it once
            viewModel.setErrorMessageShownToTrue()
        }
    }
}