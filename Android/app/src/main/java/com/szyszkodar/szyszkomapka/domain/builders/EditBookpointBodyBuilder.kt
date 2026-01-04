package com.szyszkodar.szyszkomapka.domain.builders

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.EditBookpointBody

interface EditBookpointBodyBuilder {
    fun addTitle(title: String)
    fun addDescription(description: String)
    fun addLat(lat: Double)
    fun addLon(lon: Double)
    fun build(): EditBookpointBody
}