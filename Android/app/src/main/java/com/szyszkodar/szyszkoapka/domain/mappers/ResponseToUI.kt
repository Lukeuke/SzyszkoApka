package com.szyszkodar.szyszkoapka.domain.mappers

import com.szyszkodar.szyszkoapka.domain.remote.response.Response
import com.szyszkodar.szyszkoapka.domain.uiClasses.UIClass

// Interface to create ApiResponse -> UIClass mappers for specific use cases
interface ResponseToUI<in T: Response, out O: UIClass> {
    fun convert(response: T): O
}