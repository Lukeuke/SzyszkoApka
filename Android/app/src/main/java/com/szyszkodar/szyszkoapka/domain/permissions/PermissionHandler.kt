package com.szyszkodar.szyszkoapka.domain.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat
import javax.inject.Inject

abstract class PermissionHandler @Inject constructor(
    private val appPermission: AppPermission,
    private val context: Context
) {
    @Composable
    fun ShowDialog(onGranted: () -> Unit, onDenied: () -> Unit) {
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if(granted) {
                onGranted()
            } else {
                onDenied()
            }
        }

        if (checkIfGranted()) {
            LaunchedEffect(Unit) {
                permissionLauncher.launch(appPermission.systemPermission)
            }
        }
    }

    private fun checkIfGranted(): Boolean {
        return ContextCompat.checkSelfPermission(context, appPermission.systemPermission) == PackageManager.PERMISSION_GRANTED
    }
}