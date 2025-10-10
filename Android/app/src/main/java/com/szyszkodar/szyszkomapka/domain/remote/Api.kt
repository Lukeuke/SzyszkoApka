package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkomapka.data.remote.response.IdentityResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
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
        @Body passwordChangeBody: PasswordChangeBody
    )

    @DELETE("book-points/{id}")
    suspend fun deleteBookpoints(
        @Path("id") id: String
    )

    @POST("book-points/approve/{id}")
    suspend fun approveBookpoint(
        @Path("id") id: String
    )

    @POST("book-points")
    suspend fun createBookpoint(
        @Body createBookpointBody: CreateBookpointBody
    )

    @GET("images/{id}")
    suspend fun getImage(
        @Path("id") id: String
    )

    @Multipart
    @POST("images")
    suspend fun sendImage(
        @Part file: MultipartBody.Part
    )
}