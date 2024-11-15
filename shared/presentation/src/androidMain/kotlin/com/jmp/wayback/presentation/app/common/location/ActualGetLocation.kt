package com.jmp.wayback.presentation.app.common.location

import org.koin.java.KoinJavaComponent.inject

actual suspend fun getLocation(): Location? {
    val locationProvider: LocationProvider by inject(LocationProvider::class.java)
    val isPermissionGranted = locationProvider.checkAndRequestLocationPermission()

    return if (!isPermissionGranted) {
        null
    }
    else {
        locationProvider.getUserLocation()
    }
}
