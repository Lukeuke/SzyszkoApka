package com.szyszkodar.szyszkomapka.presentation.administratorScreen


import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.szyszkodar.szyszkomapka.data.mappers.BookpointsMapper
import com.szyszkodar.szyszkomapka.data.remote.paging.ResponsePager
import com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkomapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdministratorScreenViewModel @Inject constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {
    private val _state = MutableStateFlow(AdministratorScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AdministratorScreenState()
    )

    init {
        fetchBookpoints()
    }


    fun fetchBookpoints(query: GetBookpointsQuery = GetBookpointsQuery(filters = emptyList())) {

        val mapper = BookpointsMapper()
        setIsLoading(true)

        val bookpoints = ResponsePager(20) { page ->
            val bookpointsQuery = query.copy(page = page, pageSize = 20)
            val result = bookpointsRepository.getBookpoints(bookpointsQuery)

            if (result is Result.Error) {
                _state.update { it.copy(errorMessage = result.error.message) }
            }

            setIsLoading(false)

            return@ResponsePager result
        }.pager.map { pagingData -> pagingData.map { el -> mapper.convert(el) } }
            .cachedIn(viewModelScope)

        _state.update { it.copy(bookPoints = bookpoints) }
    }

    fun deleteBookpoint(bookpoint: BookpointUI) {
        setIsLoading(true)

        viewModelScope.launch {

            val result = bookpointsRepository.deleteBookpoint(bookpoint.id)

            when (result) {
                is Result.Error -> { setToastMessage(result.error.message) }
                is Result.Success -> {
                    setToastMessage("Pomyślnie usunięto")
                    deleteBookpointLocally(bookpoint)
                }
            }

            setIsLoading(false)
        }
    }

    fun acceptBookpoint(bookpoint: BookpointUI) {
        setIsLoading(true)

        viewModelScope.launch {
            val result = bookpointsRepository.approveBookpoint(id = bookpoint.id)

            when(result) {
                is Result.Error -> { setToastMessage(result.error.message) }
                is Result.Success -> { setToastMessage("Pomyślnie zaakceptowano") }
            }
            setIsLoading(false)
        }

    }

    private fun setIsLoading(value: Boolean) {
        _state.update { it.copy(isLoading = value) }
    }

    private fun deleteBookpointLocally(bookpoint: BookpointUI) {
        val tempList = _state.value.bookPoints.map { pagingData ->
            pagingData.filter { it != bookpoint }
        }

        _state.update { it.copy(bookPoints = tempList) }
    }

    fun setMessageNull() {
        _state.update { it.copy(toastMessage = null) }
    }


    fun updateSearchValue(newValue: TextFieldValue) {
        _state.update { it.copy(searchValue = newValue) }
    }

    private fun setToastMessage(message: String) {
        _state.update { it.copy(toastMessage = message) }
    }
}

