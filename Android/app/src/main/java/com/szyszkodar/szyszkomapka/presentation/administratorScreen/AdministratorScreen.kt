package com.szyszkodar.szyszkomapka.presentation.administratorScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.szyszkodar.szyszkomapka.data.remote.filter.BookpointsFilter
import com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.FieldParam
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.OperatorParam
import com.szyszkodar.szyszkomapka.presentation.administratorScreen.components.BookpointListItem
import com.szyszkodar.szyszkomapka.presentation.shared.icons.Filter

@Composable
fun AdministratorScreen(
    token: String?,
    onExitClick: () ->Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val viewModel: AdministratorScreenViewModel = hiltViewModel()
    val state = viewModel.state.collectAsStateWithLifecycle()
    val pagingItems = state.value.bookPoints.collectAsLazyPagingItems()
    var filtersVisible by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp, vertical = 60.dp)
            .shadow(
                elevation = 1.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Zarządzanie biblioteczkami",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                IconButton(
                    onClick = onExitClick
                ) {
                    Icon(Icons.Default.Clear, null)
                }
            }

            Column (
                modifier = Modifier
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
            ){
                TextField(
                    value = state.value.searchValue,
                    onValueChange = {
                        viewModel.updateSearchValue(it)
                        viewModel.fetchBookpoints(GetBookpointsQuery(filters = listOf(BookpointsFilter(FieldParam.TITLE, OperatorParam.ILIKE, "%${it.text}%"))))
                    },
                    placeholder = { Text(text = "Wyszukaj...") },
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                    ),
                    suffix = {
                        Icon(
                            imageVector = Filter,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .clickable {
                                    filtersVisible = !filtersVisible
                                }
                        )
                    },
                )

                AnimatedVisibility(
                    visible = filtersVisible
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = Color(0xFFE8E5E5),
                                shape = RoundedCornerShape(bottomEnd = 16.dp, bottomStart = 16.dp)
                            )
                            .padding(horizontal = 10.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                Text("Zaakceptowano")
                            }
                            Button(
                                onClick = {},
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(4.dp))
                            ) {
                                Text("Oczekuje")
                            }
                        }
                    }
                }
            }
        if (state.value.isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        } else {
                Spacer(Modifier.height(30.dp))

                LazyColumn {
                    items(pagingItems.itemCount) { index ->
                        val bookpoint = pagingItems[index]

                        if (bookpoint != null && bookpoint.title.lowercase().contains(state.value.searchValue.text.lowercase())) {
                            BookpointListItem(
                                bookpointUI = bookpoint,
                                deleteBookpointFunction = { token?.let {
                                    viewModel.deleteBookpoint(bookpoint, it)
                                } }
                            )
                        }
                    }

                    when(pagingItems.loadState.append) {
                        is LoadState.Loading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    LinearProgressIndicator()
                                }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                Text(
                                    text = "Błąd ładowania danych",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }
                        is LoadState.NotLoading -> {}
                    }
                }
            }
        }
    }

    state.value.toastMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        viewModel.setMessageNull()
    }
}