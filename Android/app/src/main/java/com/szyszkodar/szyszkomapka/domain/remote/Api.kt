package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkomapka.data.remote.response.IdentityResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

// Interface used by Retrofit to create api requests
interface Api {
    @GET("book-points")
    suspend fun getBooks(
        @QueryMap query: Map<String, String>
    ): BookpointsResponse

    @POST("identity")
    suspend fun identity(
        @Body identityBody: IdentityBody
    ): IdentityResponse

    @POST("password-change")
    suspend fun passwordChange(
        @Header("Authorization") bearerToken: String,
        @Body passwordChangeBody: PasswordChangeBody
    )

    @DELETE("book-points/{id}")
    suspend fun deleteBookpoints(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String
    )

    @POST("book-points/approve/{id}")
    suspend fun approveBookpoint(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String
    )
}