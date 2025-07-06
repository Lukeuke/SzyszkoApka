package com.szyszkodar.szyszkoapka.presentation.mapScreen

import androidx.lifecycle.ViewModel
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject  constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {

}