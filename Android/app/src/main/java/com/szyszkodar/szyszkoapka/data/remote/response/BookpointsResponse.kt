package com.szyszkodar.szyszkoapka.data.remote.response

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import com.szyszkodar.szyszkoapka.domain.remote.response.ResponseList
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

data class BookpointResponse(
    val id: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
): Response

data class BookpointsResponseList(
    @SerializedName("data")
    val data: List<BookpointResponse>,
    @SerializedName("total")
    val total: Int
): ResponseList