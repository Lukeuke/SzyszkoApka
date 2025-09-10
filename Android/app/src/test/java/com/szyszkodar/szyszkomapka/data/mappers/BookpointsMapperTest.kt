package com.szyszkodar.szyszkomapka.data.mappers

import com.szyszkodar.szyszkomapka.data.remote.response.BookpointResponseElement
import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class BookpointsMapperTest {
    private val mapper = BookpointsMapper()

    @Test
    fun isCorrectlyMapped() {
        val createdAtInstant = "2024-08-10T12:00:00Z"
        val updatedAtInstant = "2024-08-11T15:30:00Z"

        val response = BookpointResponseElement(
            id = "123",
            lat = 30.5,
            lon = 34.5,
            createdAt = createdAtInstant,
            updatedAt = updatedAtInstant,
            title = "title",
            description = "description",
            approved = true
        )

        val result = mapper.convert(response)

        assertEquals("123", result.id)
        assertEquals(30.5, result.latitude)
        assertEquals(34.5, result.longitude)
        assertEquals("title", result.title)
        assertEquals("description", result.description)
        assertEquals(true, result.approved)

        val expectedCreatedAt = ZonedDateTime.parse(createdAtInstant).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        val expectedUpdatedAt = ZonedDateTime.parse(updatedAtInstant).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()

        assertEquals(expectedCreatedAt, result.createdAt)
        assertEquals(expectedUpdatedAt, result.updatedAt)
    }

    @Test
    fun parsingDateStrCorrectly() {
        val dateStr = "2025-01-01T00:00:00Z"
        val response = BookpointResponseElement(
            id = "1",
            lat = 0.0,
            lon = 0.0,
            createdAt = dateStr,
            updatedAt = dateStr,
            title = "title",
            description = "description",
            approved = false
        )

        val result = mapper.convert(response)
        val expected = ZonedDateTime.parse(dateStr).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
        assertEquals(expected, result.createdAt)
        assertEquals(expected, result.updatedAt)
    }
}