package com.szyszkodar.szyszkoapka.data.mappers

import com.szyszkodar.szyszkoapka.data.remote.response.BookpointResponse
import com.szyszkodar.szyszkoapka.data.uiClasses.BookpointsUI
import com.szyszkodar.szyszkoapka.domain.mappers.ResponseToUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class BookpointsMapper: ResponseToUI<BookpointResponse, BookpointsUI> {
    override fun convert(response: BookpointResponse): BookpointsUI {
        return BookpointsUI(
            id = response.id,
            latitude = response.lat.toLong(),
            longitude = response.lat.toLong(),
            createdAt = response.createdAt.convertToTime(),
            updatedAt = response.updatedAt.convertToTime()
        )
    }

    private fun String.convertToTime(): LocalDateTime {
        val dateTime = Instant.parse(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return dateTime
    }
}