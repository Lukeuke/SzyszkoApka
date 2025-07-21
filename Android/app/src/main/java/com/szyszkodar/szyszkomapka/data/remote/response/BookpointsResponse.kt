package com.szyszkodar.szyszkomapka.data.remote.response

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkomapka.domain.remote.response.Response
import com.szyszkodar.szyszkomapka.domain.remote.response.ResponseList

data class BookpointResponse(
    val id: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    val description: String,
    val approved: Boolean
): Response

data class BookpointsResponseList(
    val data: List<BookpointResponse>,
    val total: Int
): ResponseList