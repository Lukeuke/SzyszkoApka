package com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szyszkodar.szyszkomapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookpointInfoBottomSheetViewModel @Inject constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {
    private val _state = MutableStateFlow(BookpointInfoBottomSheetState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BookpointInfoBottomSheetState()
    )

    fun approveBookpoint(bookpoint: BookpointUI, token: String, onSuccess: () -> Unit) {
        toggleLoading(true)
        viewModelScope.launch {
            when(val response = bookpointsRepository.approveBookpoint(bookpoint.id)) {
                is Result.Success -> {
                    setToastMessage("Pomyślnie dodano biblioteczkę")
                    onSuccess()
                }
                is Result.Error -> {
                    setToastMessage(response.error.message)
                }
            }
            toggleLoading(false)
        }
    }

    fun setToastMessage(message: String? = null) {
        _state.update { it.copy(toastMessage = message) }
    }

    private fun toggleLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }
}