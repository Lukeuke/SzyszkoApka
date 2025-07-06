package com.szyszkodar.szyszkoapka.domain.remote

import com.szyszkodar.szyszkoapka.data.remote.response.BookpointsResponseList
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList

sealed class ApiRequest<out T: ResponseList> {
    data object GetBookpoints: ApiRequest<BookpointsResponseList>()
}