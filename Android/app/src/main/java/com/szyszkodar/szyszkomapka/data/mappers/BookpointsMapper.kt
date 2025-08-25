package com.szyszkodar.szyszkomapka.data.mappers

import com.szyszkodar.szyszkomapka.data.remote.response.BookpointResponseElement
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.mappers.ResponseToUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

// Mapper for BookpointsResponse -> BookpointsUI operation
class BookpointsMapper: ResponseToUI<BookpointResponseElement, BookpointUI> {
    override fun convert(response: BookpointResponseElement): BookpointUI {
        return BookpointUI(
            id = response.id,
            latitude = response.lat,
            longitude = response.lon,
            createdAt = response.createdAt.convertToTime(),
            updatedAt = response.updatedAt.convertToTime(),
            title = response.title,
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