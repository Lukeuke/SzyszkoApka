package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject  constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {
    init {
        viewModelScope.launch {
            val x = bookpointsRepository.fetchData()
            Log.d("aa", x.data.toString())
        }
    }
}