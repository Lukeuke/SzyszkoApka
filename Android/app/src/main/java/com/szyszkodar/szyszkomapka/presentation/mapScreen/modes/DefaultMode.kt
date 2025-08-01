package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.BookpointBottomSheet
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.MapLibreView
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar
import org.maplibre.android.maps.MapView

@Composable
fun DefaultMode(
    paddingValues: PaddingValues,
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
    ) {
        AnimatedVisibility(state.value.bookpointInfoVisible) {
            BookpointBottomSheet(state.value.chosenBookpoint, {
                viewModel.toggleBookpointVisibility()
            })
        }

        TopBar(
            text = "SzyszkoMapka",
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}