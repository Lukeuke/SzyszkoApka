package com.szyszkodar.szyszkomapka.data.remote.builders

import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
import com.szyszkodar.szyszkomapka.data.remote.body.EditBookpointBody
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.builders.EditBookpointBodyBuilder

class EditBookpointBodyBuilder: EditBookpointBodyBuilder {
    private var _body = EditBookpointBody(
        lat = 0f,
        lon = 0f,
        title = "",
        description = "",
        approved = false
    )

    constructor(bookpointUI: BookpointUI) {
        _body = _body.copy(
            lat = bookpointUI.latitude.toFloat(),
            lon = bookpointUI.longitude.toFloat(),
            title = bookpointUI.title,
            description = bookpointUI.description,
            approved = bookpointUI.approved
        )
    }

    override fun addTitle(title: String) {
        _body = _body.copy(title = title)
    }

    override fun addDescription(description: String) {
        _body = _body.copy(description = description)
    }

    override fun addLat(lat: Double) {
        _body = _body.copy(lat = lat.toFloat())
    }

    override fun addLon(lon: Double) {
        _body = _body.copy(lon = lon.toFloat())
    }

    override fun build(): EditBookpointBody =
        _body

}