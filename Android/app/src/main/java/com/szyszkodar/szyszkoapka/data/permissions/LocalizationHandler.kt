package com.szyszkodar.szyszkoapka.data.permissions

import android.content.Context
import com.szyszkodar.szyszkoapka.domain.permissions.AppPermission
import com.szyszkodar.szyszkoapka.domain.permissions.PermissionHandler

class LocalizationHandler(context: Context): PermissionHandler(AppPermission.LOCALIZATION, context)