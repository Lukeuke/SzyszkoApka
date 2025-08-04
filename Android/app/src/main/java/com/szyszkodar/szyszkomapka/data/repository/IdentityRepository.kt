package com.szyszkodar.szyszkomapka.data.repository

import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody
import com.szyszkodar.szyszkomapka.data.remote.response.IdentityResponse
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.repository.Repository
import javax.inject.Inject

// Identity repository - use it to make call for identity related requests
class IdentityRepository @Inject constructor(
    api: Api
): Repository(
    api = api
) {
    suspend fun getIdentity(body: IdentityBody): Result<IdentityResponse, NetworkError> {
        return request(ApiRequest.CheckIdentity(body))
    }

    suspend fun changePassword(token: String, body: PasswordChangeBody): Result<Unit, NetworkError> {
        return request(ApiRequest.PasswordChange(token, body))
    }
}