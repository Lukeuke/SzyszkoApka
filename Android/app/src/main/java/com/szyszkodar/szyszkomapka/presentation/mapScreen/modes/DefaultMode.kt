package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.BookpointBottomSheet
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.FloatingButtonsColumn
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar
import kotlinx.coroutines.launch

@Composable
fun DefaultMode(
    paddingValues: PaddingValues,
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val rotation = remember { Animatable(0f) }
    var listExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val density = LocalDensity.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            var mainButtonHeight by remember { mutableStateOf(0.dp) }
            val buttonsListOffset = animateDpAsState(
                targetValue = if (listExpanded) 0.dp else mainButtonHeight,
                animationSpec = tween(400)
            )

            AnimatedVisibility(
                visible = listExpanded,
                enter = expandVertically(expandFrom = Alignment.Top),
                exit = shrinkVertically(shrinkTowards = Alignment.Top),
                modifier = Modifier
                    .offset(y = buttonsListOffset.value)
            ) {
                FloatingButtonsColumn(
                    firstButtonName = "Logowanie",
                    firstButtonIcon = Icons.Default.Face,
                    firstButtonColor = MaterialTheme.colorScheme.primary,
                    onFirstButtonClick = {},
                    secondButtonName = "Dodaj Biblioteczkę",
                    secondButtonIcon = Icons.Default.Add,
                    secondButtonColor = MaterialTheme.colorScheme.primary,
                    onSecondButtonClick = {
                        viewModel.changeAppMode(AppMode.ADD_BOOKPOINT)
                    },
                    buttonsSize = mainButtonHeight - 10.dp
                )
            }

            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.secondary,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                modifier = Modifier
                    .padding(10.dp)
                    .onGloballyPositioned { layoutCoordinates ->
                        mainButtonHeight = with(density) { layoutCoordinates.size.height.toDp() }
                    },
                onClick = {
                    scope.launch {
                        rotation.animateTo(
                            targetValue = if(rotation.value > 0) 0f else 180f,
                            animationSpec = tween(400)
                        )
                    }

                    listExpanded = !listExpanded
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    modifier = Modifier
                        .rotate(rotation.value)
                )
            }
        }

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