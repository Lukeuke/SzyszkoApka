package com.szyszkodar.szyszkomapka.data.permissions

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.szyszkodar.szyszkomapka.domain.permissions.AppPermission
import com.szyszkodar.szyszkomapka.domain.permissions.PermissionHandler
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import org.maplibre.android.geometry.LatLng

class LocalizationHandler(private val context: Context): PermissionHandler(AppPermission.LOCALIZATION, context) {
    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    suspend fun getUserLocalization(): LatLng? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        return if (checkIfGranted() && checkIfGPSEnabled()) {
            val location = fusedLocationClient.lastLocation.await()
            location?.let { LatLng(it.latitude, it.longitude) }
        } else {
            null
        }
    }

    fun observeUserLocation(): Flow<LatLng?> = callbackFlow {
        if(!checkIfGranted() || !checkIfGPSEnabled()) {
            trySend(null)
            close()
            return@callbackFlow
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1500)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(500)
            .setMaxUpdateDelayMillis(1000)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                val location =  p0.lastLocation
                location?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
        } catch (e: SecurityException) {
            trySend(null)
            close(e)
        }
        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    private fun checkIfGPSEnabled(): Boolean {
        return locationManager.isProviderEnabled(
            LocationManager.GPS_PROVIDER
        ) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}