package com.szyszkodar.szyszkoapka.data.mappers

import com.szyszkodar.szyszkoapka.data.remote.response.BookpointResponse
import com.szyszkodar.szyszkoapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkoapka.domain.mappers.ResponseToUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// Mapper for BookpointsResponse -> BookpointsUI operation
class BookpointsMapper: ResponseToUI<BookpointResponse, BookpointUI> {
    override fun convert(response: BookpointResponse): BookpointUI {
        return BookpointUI(
            id = response.id,
            latitude = response.lat,
            longitude = response.lat,
            createdAt = response.createdAt.convertToTime(),
            updatedAt = response.updatedAt.convertToTime(),
            description = response.description,
            approved = response.approved
        )
    }

    // Local function created to convert String to LocalDateTime
    private fun String.convertToTime(): LocalDateTime {
        val dateTime = Instant.parse(this)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()

        return dateTime
    }
}