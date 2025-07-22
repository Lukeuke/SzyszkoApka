package com.szyszkodar.szyszkomapka.data.remote.body

import com.google.gson.annotations.SerializedName
import com.szyszkodar.szyszkomapka.domain.remote.body.Body

data class PasswordChangeBody(
    @SerializedName("old_password") val oldPassword: String,
    @SerializedName("new_password") val newPassword: String
): Body
