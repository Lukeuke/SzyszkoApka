package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.szyszkodar.szyszkomapka.R
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
        Image(
            painter = painterResource(R.drawable.add_bookpoint_marker),
            contentDescription = null,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier
                .align(Alignment.Center)
        )

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