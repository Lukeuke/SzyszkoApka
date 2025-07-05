package com.szyszkodar.szyszkoapka.domain.repository

import com.szyszkodar.szyszkoapka.domain.remote.response.Response

interface Repository {
    suspend fun fetchData(): Response
}