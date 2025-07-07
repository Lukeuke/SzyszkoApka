package com.szyszkodar.szyszkoapka.domain.remote

import com.szyszkodar.szyszkoapka.BuildConfig
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
import retrofit2.http.GET
import retrofit2.http.Headers

interface Api {
    @Headers("X-App-ID: ${BuildConfig.APP_ID}")
    @GET("book-points")
    suspend fun getBooks(): BookpointsResponseList
}