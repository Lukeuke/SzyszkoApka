package com.szyszkodar.szyszkomapka.presentation.LogInForm

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.repository.IdentityRepository
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogInFormViewModel @Inject constructor(
    private val identityRepository: IdentityRepository
): ViewModel() {
    private val _state =  MutableStateFlow(LogInFormState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LogInFormState()
    )

    fun updatePassword(text: TextFieldValue) {
        _state.update { it.copy(password = text) }
    }

    fun updateUsername(text: TextFieldValue) {
        _state.update { it.copy(username = text) }
    }

    fun verifyCredentials() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val body = IdentityBody(
                username = state.value.username.text,
                password = state.value.password.text
            )

            when(val response = identityRepository.getIdentity(body)) {
                is Result.Success -> {}
                is Result.Error -> {
                    when(response.error) {
                        NetworkError.IDENTITY_ERROR -> {}
                        else -> {}
                    }
                }
            }

            _state.update { it.copy(isLoading = false) }
        }
    }
}