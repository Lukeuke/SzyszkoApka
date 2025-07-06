package com.szyszkodar.szyszkoapka.domain.repository

import com.szyszkodar.szyszkoapka.data.remote.Api
import com.szyszkodar.szyszkoapka.data.remote.MakeApiCall
import com.szyszkodar.szyszkoapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkoapka.domain.errorHandling.Result
import com.szyszkodar.szyszkoapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList
import retrofit2.HttpException

abstract class Repository<T: ResponseList>(
    private val api: Api,
    private val request: ApiRequest<T>
) {
    suspend fun fetchData(): Result<T, NetworkError> {
        val makeApiCall = MakeApiCall(api)

        return try {
            val result: T = makeApiCall.makeCall(request)
            Result.Success(result)
        } catch(e: HttpException) {
            when(e.code()) {
                408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
                else -> Result.Error(NetworkError.UNKNOWN)
            }
        } catch(e: Throwable) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}