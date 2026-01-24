package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szyszkodar.szyszkomapka.data.remote.body.EditBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.builders.EditBookpointBodyBuilder
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenState
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import com.szyszkodar.szyszkomapka.presentation.shared.OutlinedText
import com.szyszkodar.szyszkomapka.ui.theme.LilitaOne
import kotlinx.coroutines.launch

@Composable
fun EditBookpointForm(
    bookpoint: BookpointUI,
    exit: () -> Unit,
    editBookpoint: (String, String?, EditBookpointBody, () -> Unit) -> Unit,
    setImageToSendNull: () -> Unit,
    buttonsEnabled: Boolean,
    deleteImage: Boolean,
    saveImageAsMultipart: (Context, Uri) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val builder = remember(bookpoint.id) {
        EditBookpointBodyBuilder(bookpoint)
    }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var bookpointName by remember { mutableStateOf(TextFieldValue(bookpoint.title)) }
    var bookpointDescription by remember { mutableStateOf(TextFieldValue(bookpoint.description)) }
    var bookpointLat by remember { mutableStateOf(TextFieldValue(bookpoint.latitude.toString())) }
    var bookpointLon by remember { mutableStateOf(TextFieldValue(bookpoint.longitude.toString())) }
    var sendButtonEnabled by remember { mutableStateOf(true) }

    val textBoxOffset = remember { Animatable(0f) }
    val descriptionBoxOffset = remember { Animatable(0f) }

    val latTextBoxOffset = remember { Animatable(0f) }
    val lonTextBoxOffset = remember { Animatable(0f) }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(
                vertical = 100.dp,
                horizontal = 20.dp
            )
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            IconButton(
                onClick = exit,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(0xFFEAE5E5)
                ),
                modifier = Modifier
                    .size(16.dp)
                    .aspectRatio(1f)
                    .align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                    modifier = Modifier
                        .padding(2.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            OutlinedText(
                text = "Edytuj biblioteczke",
                fontFamily = LilitaOne,
                outlineDrawStyle = Stroke(width = 5f),
                fontSize = 26.sp,
                outlineColor = MaterialTheme.colorScheme.primary,
                fillColor = MaterialTheme.colorScheme.background,
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Nazawa biblioteczki",
                modifier = Modifier
                    .align(Alignment.Start)
            )
            TextField(
                value = bookpointName,
                enabled = buttonsEnabled,
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
                    builder.addTitle(bookpointName.text)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusRequester.requestFocus() }
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = textBoxOffset.value.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Opis biblioteczki",
                modifier = Modifier
                    .align(Alignment.Start)
            )
            TextField(
                value = bookpointDescription,
                enabled = buttonsEnabled,
                singleLine = false,
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
                    builder.addDescription(bookpointDescription.text)
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.clearFocus()
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = descriptionBoxOffset.value.dp)
                    .focusRequester(focusRequester)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Lat/Lon",
                modifier = Modifier
                    .align(Alignment.Start)
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
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
                            builder.addLat(bookpointLat.text.toDouble())
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
                        onGo = {  }
                    ),
                    onValueChange = { newValue ->
                        if (regex.matches(newValue.text)) {
                            bookpointLon = newValue
                            builder.addLon(bookpointLon.text.toDouble())
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .offset(lonTextBoxOffset.value.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            var imageChosen by remember { mutableStateOf(false) }
            val launcher = rememberLauncherForActivityResult(PickVisualMedia()) { uri ->
                uri?.let {
                    saveImageAsMultipart(context, it)
                }
            }
            Button(
                onClick = {
                    setImageToSendNull()
                    launcher.launch(PickVisualMediaRequest(PickVisualMedia.SingleMimeType("image/jpeg")))
                    imageChosen = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Dodaj zdjęcie biblioteczki",
                    color = MaterialTheme.colorScheme.background
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = deleteImage,
                    onCheckedChange = onCheckedChange
                )
                Text("Usuń zdjęcie biblioteczki")
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = {
                    scope.launch {

                    }
                    editBookpoint(
                        bookpoint.id,
                        bookpoint.images?.firstOrNull(),
                        builder.build()
                    ) {
                        Toast.makeText(context, "Edytowano biblioteczke", Toast.LENGTH_SHORT).show()
                        exit()
                    }
                },
                enabled = sendButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Edytuj",
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}
