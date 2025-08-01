package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.AddBookpointBoxForm
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar

@Composable
fun AddBookpointMode(
    paddingValues: PaddingValues,
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AddBookpointBoxForm(
            viewModel = viewModel,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )

        TopBar(
            text = "Dodaj biblioteczke",
            modifier = Modifier
                .padding(paddingValues)
        )
    }
}