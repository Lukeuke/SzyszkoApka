package com.szyszkodar.szyszkomapka.presentation.administratorScreen

import androidx.paging.PagingData
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

data class AdministratorScreenState(
    val isLoading: Boolean = false,
    val bookPoints: Flow<PagingData<BookpointUI>> = flowOf(PagingData.empty()),
    val errorMessage: String? = null,
    val toastMessage: String? = null
)
