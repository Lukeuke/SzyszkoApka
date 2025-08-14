package com.szyszkodar.szyszkomapka.presentation.LogInForm

import androidx.compose.ui.text.input.TextFieldValue

data class LogInFormState(
    val username: TextFieldValue = TextFieldValue(""),
    val password: TextFieldValue = TextFieldValue(""),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
