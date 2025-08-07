package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun FloatingButtonsColumn(
    firstButtonName: String,
    secondButtonName: String,
    firstButtonColor: Color,
    secondButtonColor: Color,
    firstButtonIcon: ImageVector,
    secondButtonIcon: ImageVector,
    onFirstButtonClick: () -> Unit,
    onSecondButtonClick: () -> Unit,
    buttonsSize: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        FloatingActionButton(
            shape = CircleShape,
            containerColor = firstButtonColor,
            elevation = FloatingActionButtonDefaults.loweredElevation(),
            modifier = Modifier
                .padding(10.dp)
                .size(buttonsSize),
            onClick = { onFirstButtonClick() }
        ) {
            Icon(
                imageVector = firstButtonIcon,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = firstButtonName,
            )
        }

        FloatingActionButton(
            shape = CircleShape,
            containerColor = secondButtonColor,
            elevation = FloatingActionButtonDefaults.loweredElevation(),
            modifier = Modifier
                .padding(10.dp)
                .size(buttonsSize),
            onClick = { onSecondButtonClick() }
        ) {
            Icon(
                imageVector = secondButtonIcon,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = secondButtonName,
            )
        }
    }
}