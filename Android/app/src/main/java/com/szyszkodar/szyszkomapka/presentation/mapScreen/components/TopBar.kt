package com.szyszkodar.szyszkomapka.presentation.mapScreen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szyszkodar.szyszkomapka.presentation.shared.OutlinedText
import com.szyszkodar.szyszkomapka.ui.theme.LilitaOne

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
    ) {
        OutlinedText(
            text = "SzyszkoMapka",
            fillColor = Color.White,
            fontFamily = LilitaOne,
            outlineColor = Color(0xFF584E48),
            textAlign = TextAlign.Center,
            fontSize = 34.sp,
            outlineDrawStyle = Stroke(
                width = 15f
            )
        )
    }
}