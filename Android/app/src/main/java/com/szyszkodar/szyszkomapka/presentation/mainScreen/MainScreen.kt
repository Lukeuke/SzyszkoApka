package com.szyszkodar.szyszkomapka.presentation.mainScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.szyszkodar.szyszkomapka.presentation.mainScreen.components.TopBar

@Composable
fun MainScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .padding(paddingValues)
    ) {
        content()
        TopBar()
    }
}