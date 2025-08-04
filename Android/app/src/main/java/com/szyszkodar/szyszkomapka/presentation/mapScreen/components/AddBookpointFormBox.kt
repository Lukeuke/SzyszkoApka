package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreenViewModel
import kotlinx.coroutines.launch

@Composable
fun AddBookpointBoxForm(
    viewModel: MapScreenViewModel,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

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
    ) {
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
        ) {
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
            var bookpointName by remember { mutableStateOf(TextFieldValue("")) }

            TextField(
                value = bookpointName,
                singleLine = true,
                placeholder = { Text("Podaj nazwę biblioteczki...") },
                textStyle = TextStyle(

                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFEAE5E5),
                    unfocusedContainerColor = Color(0xFFEAE5E5),
                    disabledContainerColor = Color(0xFFEAE5E5),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                onValueChange = {
                    bookpointName = it
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .padding(20.dp)
                    .weight(6f)
            )

            IconButton(
                onClick = {
                    coroutineScope.launch {
                        viewModel.addBookpoint(
                            name = bookpointName.text,
                            onSuccess = {
                                Toast.makeText(context, "Pomyślnie dodano biblioteczkę", Toast.LENGTH_SHORT).show()
                            },
                            onSError = { errorMessage ->
                                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                },
                modifier = Modifier
                    .weight(1f)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

    }
}