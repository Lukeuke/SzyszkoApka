package com.szyszkodar.szyszkomapka.domain.remote.query

interface Query {
    fun toMap(): Map<String, String>
}