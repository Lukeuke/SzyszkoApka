package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.BookpointBottomSheet
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar

@Composable
fun AdminMode(
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