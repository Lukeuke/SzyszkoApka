package com.szyszkodar.szyszkomapka.data.remote

import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.remote.query.Query
import com.szyszkodar.szyszkomapka.domain.remote.response.ResponseList

// Complete list of api calls - used by repository to choose what call to make
class MakeApiCall(private val api: Api) {
    suspend fun <T: ResponseList>makeCall(request: ApiRequest<T>, query: Query): T {
        return when(request) {
            is ApiRequest.GetBookpoints -> api.getBooks(query.toMap())
        } as T
    }
}