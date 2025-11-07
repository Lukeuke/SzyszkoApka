package com.szyszkodar.szyszkomapka.data.repository

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.repository.Repository
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject

// Bookpoints repository - use it to make call for bookpoints related requests
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
    ): Result<Unit, NetworkError> {
        return request(ApiRequest.DeleteBookpoints(id))
    }

    suspend fun createBookpoint(
        body: CreateBookpointBody
    ): Result<Response<Unit>, NetworkError> {
        return request(ApiRequest.CreateBookpoint(body))
    }

    suspend fun approveBookpoint(
        id: String,
    ): Result<Unit, NetworkError> {
        return request(ApiRequest.ApproveBookpoints(id = id))
    }

    suspend fun getImageById(
        id: String
    ): Result<Response<ResponseBody>, NetworkError> {
        return request(ApiRequest.GetImage(id = id))
    }

    suspend fun uploadImage(
        id: String,
        q: Int = 70,
        file: MultipartBody.Part
    ): Result<Unit, NetworkError> {
        return request(ApiRequest.UploadImage(id, q, file))
    }
}