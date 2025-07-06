package com.szyszkodar.szyszkoapka.data.repository

import android.util.Log
import com.szyszkodar.szyszkoapka.data.remote.Api
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkoapka.domain.errorHandling.NetworkError
import com.szyszkodar.szyszkoapka.domain.errorHandling.Result
import com.szyszkodar.szyszkoapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList
import com.szyszkodar.szyszkoapka.domain.repository.Repository
import javax.inject.Inject

class BookpointsRepository @Inject constructor(
    api: Api
): Repository<BookpointsResponseList>(
    api = api,
    request = ApiRequest.GetBookpoints
)