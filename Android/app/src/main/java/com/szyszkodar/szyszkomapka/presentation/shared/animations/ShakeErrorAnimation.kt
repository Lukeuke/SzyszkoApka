package com.szyszkodar.szyszkomapka.presentation.shared.animations

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween

suspend fun shakeErrorAnimation(offsetX: Animatable<Float, AnimationVector1D>) {
    val delta = 10f
    val duration = 50
    for (i in 0..2) {
        offsetX.animateTo(-delta, animationSpec = tween(durationMillis = duration))
        offsetX.animateTo(delta, animationSpec = tween(durationMillis = duration))
    }
    offsetX.animateTo(0f, animationSpec = tween(durationMillis = duration))
}