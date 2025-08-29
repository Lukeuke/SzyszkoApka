package com.szyszkodar.szyszkomapka.domain.remote.response

interface PageableResponse<R: ResponseElement>: Response {
    val data: List<R>
    val total: Int
}