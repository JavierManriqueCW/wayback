package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import org.koin.mp.KoinPlatform

actual suspend fun checkLocationPermissions(): Boolean =
    KoinPlatform.getKoin().get<LocationProvider>().checkLocationPermission()

actual suspend fun requestLocationPermissions(callback: (Boolean) -> Unit) {
    KoinPlatform.getKoin().get<LocationProvider>().requestLocationPermission(callback)
}

actual suspend fun getLocation(): Location? =
    KoinPlatform.getKoin().get<LocationProvider>().getUserLocation()
