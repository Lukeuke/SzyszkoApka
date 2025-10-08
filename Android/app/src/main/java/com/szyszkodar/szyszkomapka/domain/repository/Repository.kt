package com.szyszkodar.szyszkomapka.domain.repository

import com.szyszkodar.szyszkomapka.data.remote.MakeApiCall
import com.szyszkodar.szyszkomapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import retrofit2.HttpException
import java.net.UnknownHostException

// Repository abstract class - use it to implement repositories for specific requests
abstract class Repository(
    private val api: Api
) {
    protected suspend fun <T> request(request: ApiRequest): Result<T, NetworkError> {
        val makeApiCall = MakeApiCall(api)

        return returnRequestResult { makeApiCall.makeCall(request) }
    }

    private suspend fun <T> returnRequestResult(
        callFunction: suspend () -> T
    ): Result<T, NetworkError> {
        return try {
            val result = callFunction()
            Result.Success(result)
        } catch(e: HttpException) {
            when(e.code()) {
                404 -> Result.Error(NetworkError.IDENTITY_ERROR)
                408 -> Result.Error(NetworkError.REQUEST_TIMEOUT)
                429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
                in 500..599 -> Result.Error(NetworkError.SERVER_ERROR)
                else -> Result.Error(NetworkError.UNKNOWN)
            }
        } catch(e: UnknownHostException) {
            Result.Error(NetworkError.NO_CONNECTION)
        } catch(e: Throwable) {
            Result.Error(NetworkError.UNKNOWN)
        }
    }
}