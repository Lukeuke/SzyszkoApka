package com.szyszkodar.szyszkomapka.presentation.bookpointInfoPopUp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI

@Composable
fun BookpointInfoPopUp(
    bookpoint: BookpointUI,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .background(Color.White, RoundedCornerShape(16.dp))
            .verticalScroll(rememberScrollState())
    ) {
        Text(bookpoint.id)
        Text(bookpoint.latitude.toString())
        Text(bookpoint.longitude.toString())
        Text(bookpoint.createdAt.toString())
        Text(bookpoint.updatedAt.toString())
    }
}