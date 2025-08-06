package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.BookpointBottomSheet
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar

@Composable
fun DefaultMode(
    paddingValues: PaddingValues,
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        FloatingActionButton(
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.secondary,
            elevation = FloatingActionButtonDefaults.loweredElevation(),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            onClick = {
                if(state.value.appMode == AppMode.DEFAULT) viewModel.changeAppMode(AppMode.ADD_BOOKPOINT) else viewModel.changeAppMode(
                AppMode.DEFAULT)
            }
        ) { Icon(Icons.Default.Add, null) }

        AnimatedVisibility(state.value.bookpointInfoVisible) {
            BookpointBottomSheet(state.value.chosenBookpoint, {
                viewModel.toggleBookpointVisibility()
            })
        }

        TopBar(
            text = "SzyszkoMapka",
            modifier = Modifier
        )

    }
}