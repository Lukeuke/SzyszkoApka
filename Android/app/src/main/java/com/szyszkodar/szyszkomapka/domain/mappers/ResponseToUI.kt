package com.szyszkodar.szyszkomapka.domain.mappers

import com.szyszkodar.szyszkomapka.domain.remote.response.Response
import com.szyszkodar.szyszkomapka.domain.uiClasses.UIClass

// Interface to create ApiResponse -> UIClass mappers for specific use cases
interface ResponseToUI<in T: Response, out O: UIClass> {
    fun convert(response: T): O
}