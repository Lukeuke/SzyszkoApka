package com.szyszkodar.szyszkomapka.domain.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat

abstract class PermissionHandler (
    private val appPermission: AppPermission,
    private val context: Context
) {
    @Composable
    fun ShowDialog(onGranted: (() -> Unit)? = null, onDenied: (() -> Unit)? = null) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if(granted) {
                if (onGranted != null) {
                    onGranted()
                }
            } else {
                if (onDenied != null) {
                    onDenied()
                }
            }
        }

        if (!checkIfGranted()) {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(appPermission.systemPermission)
            }
        }
    }

    protected fun checkIfGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, appPermission.systemPermission) == PackageManager.PERMISSION_GRANTED
    }
}