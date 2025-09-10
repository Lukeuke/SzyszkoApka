package com.szyszkodar.szyszkomapka.data.remote

import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.BuildConfig.BASE_URL
import com.szyszkodar.szyszkomapka.domain.remote.Api
import okhttp3.OkHttpClient
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MakeApiCallTest {
    val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("X-App-ID", BuildConfig.API_KEY)
                .build()

            chain.proceed(newRequest)
        }
        .build()

    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(Api::class.java)
    private val makeApiCall = MakeApiCall(api = )

    @Test
    fun testReturnWhenCheckIdentityRequest() {

    }
}