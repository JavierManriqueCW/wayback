package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.presentation.app.provider.location.LocationPermissionStatus
import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.mp.KoinPlatform

actual suspend fun checkLocationPermissions(): Boolean =
    KoinPlatform.getKoin().inject<LocationProvider>().value.checkAuthorizationStatus() == LocationPermissionStatus.ACCEPTED

actual suspend fun requestLocationPermissions(callback: (Boolean) -> Unit) {
    KoinPlatform.getKoin().inject<LocationProvider>().value.requestLocationPermission(callback)
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getLocation(): Location? =
    KoinPlatform.getKoin().inject<LocationProvider>().value.run {
        requestCurrentLocation()?.run {
            if (isSuccess)
                getOrNull()?.coordinate()?.useContents {
                    Location(
                        address = getAddressFromLatLong(
                            latitude = latitude,
                            longitude = longitude
                        ),
                        latitude = latitude,
                        longitude = longitude
                    )
                }
            else null
        }
    }
