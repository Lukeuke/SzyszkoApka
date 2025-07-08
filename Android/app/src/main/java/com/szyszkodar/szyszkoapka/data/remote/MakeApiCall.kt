package com.szyszkodar.szyszkoapka.data.remote

import com.szyszkodar.szyszkoapka.domain.remote.Api
import com.szyszkodar.szyszkoapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList

// Complete list of api calls - used by repository to choose what call to make
class MakeApiCall(private val api: Api) {
    suspend fun <T: ResponseList>makeCall(request: ApiRequest<T>): T {
        return when(request) {
            is ApiRequest.GetBookpoints -> api.getBooks()
        } as T
    }
}