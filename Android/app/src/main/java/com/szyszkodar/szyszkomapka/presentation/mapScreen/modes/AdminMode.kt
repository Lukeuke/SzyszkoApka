package com.szyszkodar.szyszkomapka.presentation.mapScreen.modes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.administratorScreen.AdministratorScreen
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.BookpointBottomSheet
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.FloatingButtonsColumn
import com.szyszkodar.szyszkomapka.presentation.mapScreen.components.TopBar
import com.szyszkodar.szyszkomapka.presentation.shared.icons.MyLocationIcon
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

@Composable
fun AdminMode(
    paddingValues: PaddingValues,
    viewModel: MapScreenViewModel,
    refreshMapFunction: () -> Unit,
    localizeBookpointFunction: (LatLng) -> Unit,
    centerCameraFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    var adminScreenVisible by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }
    var listExpanded by remember { mutableStateOf(false) }
    val labelRotation = remember { Animatable(90f) }
    var buttonEnabled by remember { mutableStateOf(true) }

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

            FloatingActionButton(
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.loweredElevation(),
                modifier = Modifier
                    .padding(10.dp)
                    .onGloballyPositioned { layoutCoordinates ->
                        mainButtonHeight = with(density) { layoutCoordinates.size.height.toDp() }
                    }
                    .size(mainButtonHeight - 10.dp),
                onClick = {
                    centerCameraFunction()
                }
            ) {
                Icon(
                    imageVector = MyLocationIcon,
                    tint = MaterialTheme.colorScheme.background,
                    contentDescription = "Wycentruj",
                )
            }

            AnimatedVisibility(
                visible = listExpanded,
                enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(),
                exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
                modifier = Modifier
                    .offset(y = buttonsListOffset.value)
            ) {
                FloatingButtonsColumn(
                    firstButtonName = "Administrator",
                    firstButtonIcon = Icons.Default.Person,
                    firstButtonColor = MaterialTheme.colorScheme.primary,
                    onFirstButtonClick = {
                        adminScreenVisible = true
                    },
                    secondButtonName = "Dodaj Biblioteczke",
                    secondButtonIcon = Icons.Default.Add,
                    secondButtonColor = MaterialTheme.colorScheme.primary,
                    onSecondButtonClick = {
                        viewModel.changeAppMode(AppMode.ADD_BOOKPOINT)
                    },
                    labelRotation = labelRotation,
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
                    if(buttonEnabled) {
                        buttonEnabled = false
                        scope.launch {
                            val labelAnimation = async {
                                labelRotation.animateTo(
                                    targetValue = if(rotation.value == 0f) 0f else 90f,
                                    animationSpec = tween(300)
                                )
                            }
                            val buttonAnimation = async {
                                rotation.animateTo(
                                    targetValue = if(rotation.value > 0) 0f else 180f,
                                    animationSpec = tween(400)
                                )
                            }

                            awaitAll(labelAnimation, buttonAnimation)
                            buttonEnabled = true
                        }
                        listExpanded = !listExpanded
                    }
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

        AnimatedVisibility(state.value.chosenBookpoint != null) {
            state.value.chosenBookpoint?.let {
                BookpointBottomSheet(
                    bookpoint = it,
                    bearerToken = state.value.bearerToken,
                    refreshMapFunction = refreshMapFunction,
                    onDismissRequest = {
                        viewModel.toggleBookpointVisibility()
                    }
                )
            }
        }

        TopBar(
            text = "SzyszkoMapka",
            modifier = Modifier
        )

        AnimatedVisibility(
            visible = adminScreenVisible,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it }
        ) {
            AdministratorScreen(
                token = state.value.bearerToken,
                onExitClick = { adminScreenVisible = false },
                localizeBookpointFunction = { latLng ->
                    adminScreenVisible = false
                    localizeBookpointFunction(latLng)
                }
            )
        }

    }
}