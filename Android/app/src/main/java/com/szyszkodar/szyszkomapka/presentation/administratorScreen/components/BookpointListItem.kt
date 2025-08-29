package com.szyszkodar.szyszkomapka.presentation.administratorScreen.components

import android.graphics.drawable.Icon
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI

@Composable
fun BookpointListItem(
    bookpointUI: BookpointUI,
    deleteBookpointFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()

    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = bookpointUI.title
            )

            Row {

                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, null)
                }

                IconButton(
                    onClick = { deleteBookpointFunction() }
                ) {
                    Icon(imageVector = Icons.Default.Delete, null)
                }
            }
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Text("yo")
        }
    }
}