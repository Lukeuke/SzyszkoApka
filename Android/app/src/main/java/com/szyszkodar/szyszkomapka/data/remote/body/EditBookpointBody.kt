package com.szyszkodar.szyszkomapka.data.remote.body

import com.szyszkodar.szyszkomapka.domain.remote.body.Body

data class EditBookpointBody(
    val lat: Float,
    val lon: Float,
    val approved: Boolean,
    val title: String,
    val description: String
): Body
