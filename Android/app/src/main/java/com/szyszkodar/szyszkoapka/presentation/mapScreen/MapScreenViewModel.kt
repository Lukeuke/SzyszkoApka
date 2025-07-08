package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szyszkodar.szyszkoapka.data.mappers.BookpointsMapper
import com.szyszkodar.szyszkoapka.data.remote.filter.BookpointsFilter
import com.szyszkodar.szyszkoapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkoapka.domain.errorHandling.Result
import com.szyszkodar.szyszkoapka.domain.remote.filterParams.FieldParam
import com.szyszkodar.szyszkoapka.domain.remote.filterParams.OperatorParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject  constructor(
    private val bookpointsRepository: BookpointsRepository
): ViewModel() {
    private val _state = MutableStateFlow(MapScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = MapScreenState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        fetchBookpoints()
    }

    fun fetchBookpoints() {
        val bookpointsMapper = BookpointsMapper()

        viewModelScope.launch {
            val query = GetBookpointsQuery(
                filters = listOf(
                    BookpointsFilter.from(
                        field = FieldParam.LAT,
                        value = 2.3,
                        operator = OperatorParam.EQ
                    )
                )
            )

            val response = bookpointsRepository.fetchData(query = query)

            when(response) {
                is Result.Success -> {
                    _state.update { it.copy(bookpoints = response.data.data.map { el ->
                        bookpointsMapper.convert(el)
                    }, errorMessage = null) }
                }
                is Result.Error -> {
                    _state.update { it.copy(errorMessage = response.error.message) }
                }
            }
        }
    }
}