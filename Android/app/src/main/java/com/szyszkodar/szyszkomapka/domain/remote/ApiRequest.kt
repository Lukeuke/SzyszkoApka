package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.EditBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody
import okhttp3.MultipartBody

// Request abstraction
sealed interface ApiRequest {
    data class CheckIdentity(
        val body: IdentityBody
    ): ApiRequest

    data class GetBookpoints(
        val queryMap: Map<String, String>
    ): ApiRequest

    data class DeleteBookpoints(
        val id: String
    ): ApiRequest

    data class ApproveBookpoints(
        val id: String
    ): ApiRequest

    data class PasswordChange(
        val body: PasswordChangeBody
    ): ApiRequest

    data class CreateBookpoint(
        val createBookpointBody: CreateBookpointBody
    ): ApiRequest

    data class EditBookpoint(
        val id: String,
        val editBookpointBody: EditBookpointBody
    ): ApiRequest

    data class GetImage(
        val id: String
    ): ApiRequest

    data class UploadImage(
        val id: String,
        val q: Int,
        val file: MultipartBody.Part
    ): ApiRequest
}