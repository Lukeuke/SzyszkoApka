package com.szyszkodar.szyszkomapka.data.repository

import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.response.IdentityResponse
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.repository.Repository
import javax.inject.Inject

class IdentityRepository @Inject constructor(
    api: Api
): Repository(
    api = api
) {
    suspend fun getIdentity(body: IdentityBody): Result<IdentityResponse, NetworkError> {
        return request(ApiRequest.CheckIdentity(body))
    }
}