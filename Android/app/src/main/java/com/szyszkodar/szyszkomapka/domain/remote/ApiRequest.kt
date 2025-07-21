package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkomapka.domain.remote.response.ResponseList

// Request abstraction
sealed class ApiRequest<out T: ResponseList> {
    data object GetBookpoints: ApiRequest<BookpointsResponseList>()
}