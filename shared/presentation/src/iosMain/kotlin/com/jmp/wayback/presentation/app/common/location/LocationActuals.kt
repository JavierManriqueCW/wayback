package com.jmp.wayback.presentation.app.common.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getPlatformPresentationDependencies(): List<Module> =
    listOf()

actual suspend fun checkPermissions(): Boolean {
    val locationProvider = LocationProvider()
    return locationProvider.checkAuthorizationStatus() == LocationPermissionStatus.ACCEPTED
}

actual suspend fun requestPermissions(callback: (Boolean) -> Unit) {
    val locationProvider = LocationProvider()
    locationProvider.requestLocationPermission(callback)
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getLocation(): Location? {
    val locationProvider = LocationProvider()
    return locationProvider.run {
        requestCurrentLocation().run {
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
}
