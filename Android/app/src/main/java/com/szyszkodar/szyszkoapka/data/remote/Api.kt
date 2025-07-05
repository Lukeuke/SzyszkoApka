package com.szyszkodar.szyszkoapka.data.remote

import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponse
import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface Api {
    @Headers("X-App-ID: com.szyszkodar.szyszkoapka")
    @GET("book_points")
    suspend fun getBooks(): BookpointsResponse
}