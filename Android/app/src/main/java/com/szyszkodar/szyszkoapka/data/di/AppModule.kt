package com.szyszkodar.szyszkoapka.data.di

import com.google.gson.GsonBuilder
import com.szyszkodar.szyszkoapka.BuildConfig
import com.szyszkodar.szyszkoapka.domain.remote.Api
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkoapka.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    val baseUrl = BuildConfig.BASE_URL

    @Provides
    @Singleton
    fun provideApi(): Api {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(Api::class.java)
    }

    @Provides
    @Singleton
    fun provideBookpointsRepository(api: Api): Repository<BookpointsResponseList> {
        return BookpointsRepository(api)
    }
}