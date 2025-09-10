package com.szyszkodar.szyszkomapka.presentation.administratorScreen.components

import android.graphics.drawable.Icon
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import kotlinx.coroutines.launch

@Composable
fun BookpointListItem(
    bookpointUI: BookpointUI,
    deleteBookpointFunction: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val expandButtonRotation = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()

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
                text = bookpointUI.title,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(6f)
            )

            Row(
                modifier = Modifier
                    .weight(2f)
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            expandButtonRotation.animateTo(
                                targetValue = if(expandButtonRotation.value > 0) 0f else 180f,
                                animationSpec = tween(400)
                            )
                        }
                        expanded = !expanded
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .rotate(expandButtonRotation.value)
                    )
                }

                IconButton(
                    onClick = { deleteBookpointFunction() }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Text(
                text = bookpointUI.description,

            )
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp))
    }
}