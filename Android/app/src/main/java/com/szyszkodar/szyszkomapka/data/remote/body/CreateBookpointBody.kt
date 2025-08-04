package com.szyszkodar.szyszkomapka.data.remote.body

import com.szyszkodar.szyszkomapka.domain.remote.body.Body

data class CreateBookpointBody(
     val lat: Float,
     val lon: Float,
     val description: String
) : Body
