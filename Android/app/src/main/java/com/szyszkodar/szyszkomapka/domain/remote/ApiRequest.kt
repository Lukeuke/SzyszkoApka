package com.szyszkodar.szyszkomapka.domain.remote

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.IdentityBody
import com.szyszkodar.szyszkomapka.data.remote.body.PasswordChangeBody

// Request abstraction
sealed interface ApiRequest {
    data class CheckIdentity(
        val body: IdentityBody
    ): ApiRequest

    data class GetBookpoints(
        val queryMap: Map<String, String>
    ): ApiRequest

    data class DeleteBookpoints(
        val id: String,
        val bearerToken: String
    ): ApiRequest

    data class ApproveBookpoints(
        val id: String,
        val bearerToken: String
    ): ApiRequest

    data class PasswordChange(
        val bearerToken: String,
        val body: PasswordChangeBody
    ): ApiRequest

    data class CreateBookpoint(
        val createBookpointBody: CreateBookpointBody
    ): ApiRequest
}