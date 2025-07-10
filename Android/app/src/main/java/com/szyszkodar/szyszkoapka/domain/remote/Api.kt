package com.szyszkodar.szyszkoapka.domain.remote

import com.szyszkodar.szyszkoapka.BuildConfig
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
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