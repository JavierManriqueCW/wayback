package com.jmp.wayback.presentation.app.common.location

import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.inject

actual suspend fun checkPermissions(): Boolean {
    val locationProvider: LocationProvider by inject(LocationProvider::class.java)
    return locationProvider.checkLocationPermission()
}

actual suspend fun requestPermissions(callback: (Boolean) -> Unit) {
    val locationProvider: LocationProvider by inject(LocationProvider::class.java)
    return locationProvider.requestLocationPermission(callback)
}

actual suspend fun getLocation(): Location? {
    val locationProvider: LocationProvider by inject(LocationProvider::class.java)
    return locationProvider.getUserLocation()
}

actual fun getPlatformPresentationDependencies(): List<Module> =
    listOf(module { single { LocationProvider() } })
