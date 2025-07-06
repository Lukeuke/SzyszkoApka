package com.szyszkodar.szyszkoapka.presentation.mapScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MapScreen(
    modifier: Modifier = Modifier
) {
    val viewmodel = hiltViewModel<MapScreenViewModel>()

    Text("siema")
}