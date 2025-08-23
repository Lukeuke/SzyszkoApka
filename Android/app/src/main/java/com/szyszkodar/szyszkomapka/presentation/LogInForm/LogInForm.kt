package com.szyszkodar.szyszkomapka.presentation.LogInForm

import android.widget.Toast
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.szyszkodar.szyszkomapka.presentation.shared.OutlinedText
import com.szyszkodar.szyszkomapka.presentation.shared.animations.shakeErrorAnimation
import com.szyszkodar.szyszkomapka.ui.theme.LilitaOne
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun LogInForm(
    onExitClick: () -> Unit,
    setBearerTokenFunction: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val viewModel: LogInFormViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()

    val loginTextFieldOffset = remember { Animatable(0f) }
    val passwordTextFieldOffset = remember { Animatable(0f) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 60.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        if (state.value.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    IconButton(
                        onClick = onExitClick,
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
                OutlinedText(
                    text = "Zaloguj sie kontem administratora",
                    fontSize = 24.sp,
                    fontFamily = LilitaOne,
                    fillColor = MaterialTheme.colorScheme.background,
                    outlineColor = MaterialTheme.colorScheme.primary,
                    outlineDrawStyle = Stroke(8f),
                    modifier = Modifier
                        .weight(3f)
                )
                TextField(
                    value = state.value.username,
                    onValueChange = {
                        viewModel.updateUsername(it)
                    },
                    singleLine = true,
                    placeholder = { Text(
                        text = "Login"
                    ) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =  Color(0xFFEAE5E5),
                        unfocusedContainerColor = Color(0xFFEAE5E5),
                        disabledContainerColor = Color(0xFFEAE5E5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1.5f)
                        .offset(x = loginTextFieldOffset.value.dp)
                )
                Spacer(Modifier.weight(0.5f))
                TextField(
                    value = state.value.password,
                    onValueChange = {
                        viewModel.updatePassword(it)
                    },
                    singleLine = true,
                    visualTransformation = VisualTransformation {
                        TransformedText(
                            AnnotatedString("*".repeat(it.text.length)),
                            offsetMapping = OffsetMapping.Identity
                        )
                    },
                    placeholder = { Text(
                        text = "Hasło"
                    ) },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            // TODO: Log in
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor =  Color(0xFFEAE5E5),
                        unfocusedContainerColor = Color(0xFFEAE5E5),
                        disabledContainerColor = Color(0xFFEAE5E5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    // TODO: Suffix with password visibility swap
                    modifier = Modifier
                        .weight(1.5f)
                        .offset(x = loginTextFieldOffset.value.dp)
                )
                if (state.value.incorrectCredentials) {
                    Text(
                        text = "Podano nieprawidłowy login lub hasło",
                        color = Color(0xFFCD372C),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .padding(vertical = 5.dp)
                            .align(Alignment.Start)
                            .pointerInput(Unit) {
                                // TODO: PasswordChange
                            }
                            .weight(1f)
                    )
                }
                IconButton(
                    onClick = {
                        if(state.value.username.text.isEmpty()) {
                            scope.launch {
                                shakeErrorAnimation(loginTextFieldOffset)
                            }
                        } else if(state.value.password.text.isEmpty()) {
                            scope.launch {
                                shakeErrorAnimation(passwordTextFieldOffset)
                            }
                        } else {
                            viewModel.verifyCredentials(setBearerTokenFunction)
                        }
                    },
                    modifier = Modifier
                        .weight(3f)
                        .padding(top = 20.dp)
                        .align(Alignment.End)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.weight(4f))
            }

        }
    }
}