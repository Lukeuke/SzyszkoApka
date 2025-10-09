package com.szyszkodar.szyszkomapka.presentation.administratorScreen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.sp
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.presentation.shared.icons.Eye
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

@Composable
fun BookpointListItem(
    bookpointUI: BookpointUI,
    deleteBookpointFunction: () -> Unit,
    localizeBookpointFunction: (LatLng) -> Unit,
    acceptBookpoint: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val expandButtonRotation = remember { Animatable(0f) }

    val coroutineScope = rememberCoroutineScope()
    val themeColor = if (bookpointUI.approved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

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
                fontSize = 16.sp,
                color = themeColor,
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
                        tint = themeColor,
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
                        tint = themeColor
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = expanded,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 10.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    if (!bookpointUI.approved) {
                        IconButton(
                            onClick = { acceptBookpoint() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = themeColor
                            )
                        }
                    }

                    IconButton(
                        onClick = { localizeBookpointFunction(LatLng(bookpointUI.latitude, bookpointUI.longitude)) }
                    ) {
                        Icon(
                            imageVector = Eye,
                            contentDescription = null,
                            tint = themeColor
                        )
                    }
                }
                Text(
                    text = bookpointUI.description,
                    fontSize = 14.sp
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 15.dp))
    }
}