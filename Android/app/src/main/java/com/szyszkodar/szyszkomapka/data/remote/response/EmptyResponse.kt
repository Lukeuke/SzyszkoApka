package com.szyszkodar.szyszkomapka.data.remote.response

import com.szyszkodar.szyszkomapka.domain.remote.response.Response

data class EmptyResponse(
    val isSuccess: Boolean = true
): Response
