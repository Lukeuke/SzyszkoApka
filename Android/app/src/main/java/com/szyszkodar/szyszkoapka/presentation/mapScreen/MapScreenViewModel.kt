package com.szyszkodar.szyszkoapka.presentation.mapScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.szyszkodar.szyszkoapka.data.remote.MakeApiCall
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkoapka.domain.remote.Api
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject  constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {

}