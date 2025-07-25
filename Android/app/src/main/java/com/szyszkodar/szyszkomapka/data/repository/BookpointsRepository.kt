package com.szyszkodar.szyszkomapka.data.repository

import com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkomapka.data.remote.response.EmptyResponse
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.repository.Repository
import javax.inject.Inject

// Bookpoints repository - use it to make call for
class BookpointsRepository @Inject constructor(
    api: Api
): Repository(
    api = api
) {
    suspend fun getBookpoints(query: GetBookpointsQuery): Result<BookpointsResponse, NetworkError> {
        return request(ApiRequest.GetBookpoints(query.toMap()))
    }

    suspend fun deleteBookpoint(
        id: String,
        bearerToken: String
    ): Result<EmptyResponse, NetworkError> {
        return request(ApiRequest.DeleteBookpoints(id, bearerToken))
    }
}