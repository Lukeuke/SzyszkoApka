package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.shared.animations.shakeErrorAnimation
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView

@Composable
fun AddBookpointBoxForm(
    viewModel: MapScreenViewModel,
    mapView: MapView,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var bookpointName by remember { mutableStateOf(TextFieldValue("")) }
    var bookpointDescription by remember { mutableStateOf(TextFieldValue("")) }
    var bookpointLat by remember { mutableStateOf(TextFieldValue("")) }
    var bookpointLon by remember { mutableStateOf(TextFieldValue("")) }
    var sendButtonEnabled by remember { mutableStateOf(true) }

    val textBoxOffset = remember { Animatable(0f) }
    val descriptionBoxOffset = remember { Animatable(0f) }

    var isExpanded by remember { mutableStateOf(false) }


    val latTextBoxOffset = remember { Animatable(0f) }
    val lonTextBoxOffset = remember { Animatable(0f) }
    val errorMessageOffset = remember { Animatable(0f) }

    var errorMessage: String? by remember { mutableStateOf(null) }

    fun onCheckClick() {
        if (bookpointLat.text.isEmpty()) {
            coroutineScope.launch {
                shakeErrorAnimation(latTextBoxOffset)
            }
        } else if (bookpointLon.text.isEmpty()) {
            coroutineScope.launch {
                shakeErrorAnimation(lonTextBoxOffset)
            }
        } else if (bookpointLon.text.toDouble() > 90 || bookpointLat.text.toDouble() > 90) {
            errorMessage = "Za duże wartości"
            coroutineScope.launch {
                shakeErrorAnimation(errorMessageOffset)
            }
        } else if(bookpointLon.text.toDouble() < 0 || bookpointLat.text.toDouble() < 0) {
            errorMessage = "Za małe wartości"
            coroutineScope.launch {
                shakeErrorAnimation(errorMessageOffset)
            }
        } else {
            isExpanded = false
            viewModel.mapViewCameraPositionChange(
                mapView = mapView,
                targetLatLng = LatLng(
                    latitude = bookpointLat.text.toDouble(),
                    longitude = bookpointLon.text.toDouble()
                )
            )
            bookpointLat = TextFieldValue("")
            bookpointLon = TextFieldValue("")
        }
    }

    fun ongoClick() {
        if(sendButtonEnabled) {
            sendButtonEnabled = false
            if(bookpointName.text.isEmpty()) {
                coroutineScope.launch {
                    shakeErrorAnimation(textBoxOffset)
                }
                sendButtonEnabled = true
            } else if(bookpointDescription.text.isEmpty()) {
                coroutineScope.launch {
                    shakeErrorAnimation(descriptionBoxOffset)
                }
                sendButtonEnabled = true
            } else {
                coroutineScope.launch {
                    viewModel.addBookpoint(
                        name = bookpointName.text,
                        description = bookpointDescription.text,
                        onSuccess = {
                            Toast.makeText(context, "Pomyślnie dodano biblioteczkę", Toast.LENGTH_SHORT).show()
                            bookpointName = TextFieldValue("")
                            bookpointDescription = TextFieldValue("")
                        },
                        onSError = { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    )
                    sendButtonEnabled = true
                }
            }

        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
            .shadow(
                elevation = 3.dp,
                spotColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
            Text(
                text = "Podaj nazwę i lokalizację",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(top = 10.dp, start = 30.dp)
                    .align(Alignment.CenterVertically)
            )
            IconButton(
                onClick = {
                    viewModel.changeAppMode(AppMode.DEFAULT)
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFEAE5E5)
                ),
                modifier = Modifier
                    .aspectRatio(1f)
                    .padding(top = 10.dp, end = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(2.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(6f)
            ) {
                TextField(
                    value = bookpointName,
                    singleLine = true,
                    placeholder = { Text(
                        text = "Nazwa biblioteczki"
                    ) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =  Color(0xFFEAE5E5),
                        unfocusedContainerColor = Color(0xFFEAE5E5),
                        disabledContainerColor = Color(0xFFEAE5E5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        bookpointName = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { ongoClick() }),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                        .offset(x = textBoxOffset.value.dp)
                )
                TextField(
                    value = bookpointDescription,
                    singleLine = false,
                    maxLines = 5,
                    placeholder = { Text(
                        text = "Opis biblioteczki"
                    ) },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =  Color(0xFFEAE5E5),
                        unfocusedContainerColor = Color(0xFFEAE5E5),
                        disabledContainerColor = Color(0xFFEAE5E5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    onValueChange = {
                        bookpointDescription = it
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                    keyboardActions = KeyboardActions(onGo = { ongoClick() }),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp)
                        .offset(x = descriptionBoxOffset.value.dp)
                )
            }


            AnimatedVisibility(
                visible = !isExpanded,
                enter = expandHorizontally(expandFrom = Alignment.Start),
                exit = shrinkHorizontally(shrinkTowards = Alignment.End)
            ) {
                IconButton(
                    onClick = { ongoClick() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 20.dp)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }

        if(!isExpanded) {
            Text(
                text = "Opcje zaawansowane",
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 10.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = 5.dp)
                    .align(Alignment.Start)
                    .pointerInput(Unit) {
                        isExpanded = true
                    }
            )
        }

        AnimatedVisibility(
            isExpanded,
            enter = expandVertically(expandFrom = Alignment.Top),
            exit = shrinkVertically(shrinkTowards = Alignment.Top)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Podaj współżędne geograficzne biblioteczki. Pamiętaj tylko, żeby były jak najbardziej dokładne.",
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(20.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val regex = Regex("^-?\\d*\\.?\\d*$")

                    TextField(
                        value = bookpointLat,
                        singleLine = true,
                        placeholder = { Text(
                            text = "Lat"
                        ) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor =  Color(0xFFEAE5E5),
                            unfocusedContainerColor = Color(0xFFEAE5E5),
                            disabledContainerColor = Color(0xFFEAE5E5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        ),
                        onValueChange = { newValue ->
                            if (regex.matches(newValue.text)) {
                                bookpointLat = newValue
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .offset(latTextBoxOffset.value.dp)
                    )

                    TextField(
                        value = bookpointLon,
                        singleLine = true,
                        placeholder = { Text(
                            text = "Lon"
                        ) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor =  Color(0xFFEAE5E5),
                            unfocusedContainerColor = Color(0xFFEAE5E5),
                            disabledContainerColor = Color(0xFFEAE5E5),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(16.dp),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Go
                        ),
                        keyboardActions = KeyboardActions(
                            onGo = { onCheckClick() }
                        ),
                        onValueChange = { newValue ->
                            if (regex.matches(newValue.text)) {
                                bookpointLon = newValue
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .offset(lonTextBoxOffset.value.dp)
                    )
                }
                AnimatedVisibility(
                    visible = errorMessage != null,
                    enter = EnterTransition.None,
                    exit = ExitTransition.None,
                    modifier = Modifier
                        .offset(errorMessageOffset.value.dp)
                        .padding(horizontal = 20.dp, vertical = 5.dp)
                ) {
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = Color(0xFFBD4A4A),
                            fontSize = 15.sp,
                            textAlign = TextAlign.Start,
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))

                OutlinedButton(
                    onClick = { onCheckClick() },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Sprawdź",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                OutlinedButton(
                    onClick = {
                        isExpanded = false
                        bookpointLat = TextFieldValue("")
                        bookpointLon = TextFieldValue("")
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Zwiń",
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }
        }
    }
}