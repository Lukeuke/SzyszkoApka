package com.szyszkodar.szyszkomapka.data.remote

import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest

// Complete list of api calls - used by repository to choose what call to make
class MakeApiCall(private val api: Api) {
    suspend fun <T> makeCall(
        request: ApiRequest,
    ): T {
        return when(request) {
            is ApiRequest.CheckIdentity -> api.identity(request.body)
            is ApiRequest.DeleteBookpoints -> api.deleteBookpoints(id = request.id)
            is ApiRequest.GetBookpoints -> api.getBooks(request.queryMap)
            is ApiRequest.ApproveBookpoints -> api.approveBookpoint(id = request.id)
            is ApiRequest.PasswordChange -> api.passwordChange(passwordChangeBody = request.body)
            is ApiRequest.CreateBookpoint -> api.createBookpoint(createBookpointBody = request.createBookpointBody)
        } as T
    }
}