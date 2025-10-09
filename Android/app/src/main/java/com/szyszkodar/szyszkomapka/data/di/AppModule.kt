package com.szyszkodar.szyszkomapka.data.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.szyszkodar.szyszkomapka.BuildConfig
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkomapka.data.repository.IdentityRepository
import com.szyszkodar.szyszkomapka.domain.remote.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

// Main app dependency injection module
// Uses Dagger Hilt
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = BuildConfig.BASE_URL

    // Provide Api object
    @Provides
    @Singleton
    fun provideApi(): Api {
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val newRequest = chain.request().newBuilder()
                    .addHeader("X-App-ID", BuildConfig.API_KEY)
                    .build()

                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(Api::class.java)
    }

    // Provide BookpointsRepository object
    @Provides
    @Singleton
    fun provideBookpointsRepository(api: Api): BookpointsRepository {
        return BookpointsRepository(api)
    }

    @Provides
    @Singleton
    fun provideIdentityRepository(api: Api): IdentityRepository {
        return IdentityRepository(api)
    }

    // Provide localization handler
    @Provides
    @Singleton
    fun provideLocalizationHandler(
        @ApplicationContext context: Context
    ): LocalizationHandler {
        return LocalizationHandler(context = context)
    }
}