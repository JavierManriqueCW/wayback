package com.jmp.wayback.presentation.app.platform

expect suspend fun getLocation(): Location?

expect suspend fun checkLocationPermissions(): Boolean

expect suspend fun requestLocationPermissions(callback: (Boolean) -> Unit)

data class Location(
    val address: String,
    val latitude: Double,
    val longitude: Double
)
