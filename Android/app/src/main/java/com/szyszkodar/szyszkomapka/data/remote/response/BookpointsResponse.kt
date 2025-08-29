package com.szyszkodar.szyszkomapka.data.remote.response

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkomapka.domain.remote.response.PageableResponse
import com.szyszkodar.szyszkomapka.domain.remote.response.Response
import com.szyszkodar.szyszkomapka.domain.remote.response.ResponseElement

data class BookpointResponseElement(
    val id: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val title: String,
    val description: String,
    val approved: Boolean
): ResponseElement

data class BookpointsResponse(
    override val data: List<BookpointResponseElement>,
    override val total: Int
): PageableResponse<BookpointResponseElement>