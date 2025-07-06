package com.szyszkodar.szyszkoapka.data.remote

import com.szyszkodar.szyszkoapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList

class MakeApiCall(private val api: Api) {
    suspend fun <T: ResponseList>makeCall(request: ApiRequest<T>): T {
        return when(request) {
            is ApiRequest.GetBookpoints -> api.getBooks()
        } as T
    }
}