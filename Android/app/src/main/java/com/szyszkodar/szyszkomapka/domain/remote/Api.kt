package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponseList
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

// Interface used by Retrofit to create api requests
interface Api {
    @Headers("X-App-ID: ${BuildConfig.API_KEY}")
    @GET("book-points")
    suspend fun getBooks(
        @QueryMap query: Map<String, String>
    ): BookpointsResponseList
}