package com.szyszkodar.szyszkomapka.presentation

import com.szyszkodar.szyszkomapka.data.enums.AppMode

data class MainState(
    val appMode: AppMode = AppMode.DEFAULT,
    val bearerToken: String? = null
)
