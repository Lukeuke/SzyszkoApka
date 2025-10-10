package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class FloatingButtonSettings(
    val name: String,
    val color: Color,
    val icon: ImageVector,
    val onClick: () -> Unit
)
