package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkomapka.data.remote.response.IdentityResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.QueryMap

// Interface used by Retrofit to create api requests
interface Api {
    @GET("book-points")
    @Headers("X-App-ID: ${BuildConfig.API_KEY}")
    suspend fun getBooks(
        @QueryMap query: Map<String, String>
    ): BookpointsResponseList

    @POST("identity")
    @Headers("X-App-ID: ${BuildConfig.API_KEY}")
    suspend fun identity(
        @Body identityBody: IdentityBody
    ): IdentityResponse

    @POST("password-change")
    @Headers("X-App-ID: ${BuildConfig.API_KEY}")
    suspend fun passwordChange(
        @Header("Authorization") bearerToken: String,
        @Body passwordChangeBody: PasswordChangeBody
    )
}