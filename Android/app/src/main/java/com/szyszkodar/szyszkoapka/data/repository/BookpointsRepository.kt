package com.szyszkodar.szyszkoapka.data.repository

import com.szyszkodar.szyszkoapka.domain.remote.Api
import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkoapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkoapka.domain.repository.Repository
import javax.inject.Inject

class BookpointsRepository @Inject constructor(
    api: Api
): Repository<BookpointsResponseList>(
    api = api,
    request = ApiRequest.GetBookpoints
)