package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.szyszkodar.szyszkomapka.presentation.shared.OutlinedText
import com.szyszkodar.szyszkomapka.ui.theme.LilitaOne

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
    labelRotation: Animatable<Float, AnimationVector1D>,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = modifier
    ) {
        ButtonWithLabel(
            buttonName = firstButtonName,
            buttonIcon = firstButtonIcon,
            buttonColor = firstButtonColor,
            buttonsSize = buttonsSize,
            labelRotation = labelRotation,
            onClick = onFirstButtonClick
        )

        ButtonWithLabel(
            buttonName = secondButtonName,
            buttonIcon = secondButtonIcon,
            buttonColor = secondButtonColor,
            buttonsSize = buttonsSize,
            labelRotation = labelRotation,
            onClick = onSecondButtonClick
        )

    }
}

@Composable
private fun ButtonWithLabel(
    buttonName: String,
    buttonIcon: ImageVector,
    buttonColor: Color,
    buttonsSize: Dp,
    labelRotation: Animatable<Float, AnimationVector1D>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End,
        modifier = modifier
            .padding(start = 20.dp)
    ) {
        OutlinedText(
            text = buttonName,
            fontFamily = LilitaOne,
            outlineDrawStyle = Stroke(width = 10f),
            fillColor = MaterialTheme.colorScheme.background,
            outlineColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .graphicsLayer (
                    rotationZ = labelRotation.value,
                    transformOrigin = TransformOrigin(1f, 1f)
                )
        )

        FloatingActionButton(
            shape = CircleShape,
            containerColor = buttonColor,
            elevation = FloatingActionButtonDefaults.loweredElevation(),
            modifier = Modifier
                .padding(10.dp)
                .size(buttonsSize),
            onClick = { onClick() }
        ) {
            Icon(
                imageVector = buttonIcon,
                tint = MaterialTheme.colorScheme.background,
                contentDescription = buttonName,
            )
        }
    }
}