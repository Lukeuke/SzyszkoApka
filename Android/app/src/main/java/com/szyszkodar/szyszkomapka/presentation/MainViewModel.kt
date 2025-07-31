package com.szyszkodar.szyszkomapka.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainState()
    )

    fun toggleAppMode(newMode: AppMode) {
        _state.update { it.copy(appMode = newMode) }
    }
}