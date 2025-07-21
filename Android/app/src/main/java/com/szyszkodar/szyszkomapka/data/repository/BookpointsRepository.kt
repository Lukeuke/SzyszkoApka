package com.szyszkodar.szyszkomapka.data.repository

import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkomapka.domain.remote.Api
import com.szyszkodar.szyszkomapka.domain.remote.ApiRequest
import com.szyszkodar.szyszkomapka.domain.repository.Repository
import javax.inject.Inject

// Bookpoints repository - use it to make call for
class BookpointsRepository @Inject constructor(
    api: Api
): Repository<BookpointsResponseList>(
    api = api,
    request = ApiRequest.GetBookpoints,
)