package com.szyszkodar.szyszkoapka.data.remote

import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import com.szyszkodar.szyszkoapka.BuildConfig

interface Api {
    @Headers("X-App-ID: ${BuildConfig.APP_ID}")
    @GET("book_points")
    suspend fun getBooks(): BookpointsResponse
}