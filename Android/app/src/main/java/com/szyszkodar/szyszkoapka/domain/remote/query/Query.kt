package com.szyszkodar.szyszkoapka.domain.remote.query

interface Query {
    fun toMap(): Map<String, String>
}