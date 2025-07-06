package com.szyszkodar.szyszkoapka.data.remote.response

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

data class Bookpoint(
    val id: String,
    val lat: Double,
    val lon: Double,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)

data class BookpointsResponse(
    @SerializedName("data")
    val data: List<Bookpoint>,
    @SerializedName("total")
    val total: Int
): Response