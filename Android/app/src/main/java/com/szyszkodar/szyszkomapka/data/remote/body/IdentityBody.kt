package com.szyszkodar.szyszkomapka.data.remote.body

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkomapka.domain.remote.body.Body

data class IdentityBody(
    @SerializedName("Username") val username: String,
    @SerializedName("Password") val password: String
): Body
