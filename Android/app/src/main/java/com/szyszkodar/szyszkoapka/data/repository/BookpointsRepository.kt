package com.szyszkodar.szyszkoapka.data.repository

import android.util.Log
import com.szyszkodar.szyszkoapka.data.remote.Api
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkoapka.domain.repository.Repository
import javax.inject.Inject

class BookpointsRepository @Inject constructor(
    private val api: Api
): Repository {
    override suspend fun fetchData(): BookpointsResponse {
        return api.getBooks()
    }
}