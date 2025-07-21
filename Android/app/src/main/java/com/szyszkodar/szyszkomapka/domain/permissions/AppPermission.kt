package com.szyszkodar.szyszkomapka.domain.permissions

import android.Manifest

enum class AppPermission(override val systemPermission: String): Permission {
    LOCALIZATION(Manifest.permission.ACCESS_FINE_LOCATION)
}