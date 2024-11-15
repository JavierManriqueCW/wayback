package com.jmp.wayback.presentation.app.common.location

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class IosLocationManager : NSObject(), CLLocationManagerDelegateProtocol {
    private val locationManager = CLLocationManager()
    private var locationPermissionStatusCancellableContinuation: CancellableContinuation<LocationPermissionStatus>? =
        null
    private var locationResultContinuation: (CancellableContinuation<Result<CLLocation>>)? = null

    init {
        locationManager.delegate = this
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    suspend fun requestCurrentLocation(): Result<CLLocation> =
        suspendCancellableCoroutine { continuation ->
            locationResultContinuation = continuation
            locationManager.requestLocation()

        }

    suspend fun requestLocationPermission(): LocationPermissionStatus =
        suspendCancellableCoroutine { continuation ->
            locationPermissionStatusCancellableContinuation = continuation
            when (CLLocationManager.authorizationStatus()) {
                kCLAuthorizationStatusNotDetermined -> {
                    locationManager.requestWhenInUseAuthorization()
                }

                kCLAuthorizationStatusRestricted, kCLAuthorizationStatusDenied -> {
                    continuation.resume(LocationPermissionStatus.RESTRICTED_OR_DENIED)
                }

                kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                    continuation.resume(LocationPermissionStatus.ACCEPTED)
                }
            }
        }


    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        locationPermissionStatusCancellableContinuation?.let {
            if (it.isActive) {
                when (didChangeAuthorizationStatus) {
                    kCLAuthorizationStatusRestricted,
                    kCLAuthorizationStatusDenied -> it.resume(
                        LocationPermissionStatus.RESTRICTED_OR_DENIED
                    )

                    kCLAuthorizationStatusAuthorizedAlways,
                    kCLAuthorizationStatusAuthorizedWhenInUse -> it.resume(
                        LocationPermissionStatus.ACCEPTED
                    )

                    kCLAuthorizationStatusNotDetermined -> it.resume(LocationPermissionStatus.NOT_DETERMINED)
                }
                locationPermissionStatusCancellableContinuation = null
            }

        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
        locationPermissionStatusCancellableContinuation?.let {
            if (it.isActive) {
                it.resume(LocationPermissionStatus.RESTRICTED_OR_DENIED)
                locationPermissionStatusCancellableContinuation = null
            }

        }
        locationResultContinuation?.let {
            if (it.isActive) {
                it.resumeWithException(Exception("Failed to get location,description:${didFailWithError.localizedDescription},code:${didFailWithError.code}"))
                locationResultContinuation = null
            }
        }
    }

    override fun locationManager(manager: CLLocationManager, didUpdateLocations: List<*>) {
        val location = didUpdateLocations.firstOrNull() as? CLLocation?
        locationResultContinuation?.let {
            if (it.isActive) {
                if (location != null) {
                    it.resume(Result.success(location))
                } else {
                    it.resumeWithException(Exception("No valid location found"))
                }
                locationResultContinuation = null
            }
        }
    }

    suspend fun getAddressFromLatLong(latitude: Double, longitude: Double): String =
        suspendCoroutine { continuation ->
        val geocoder = CLGeocoder()
        val location = CLLocation(latitude = latitude, longitude = longitude)

        geocoder.reverseGeocodeLocation(location) { placemarks, error ->
            if (error != null) {
                continuation.resumeWithException(
                    Exception(
                        error.localizedDescription ?: "Unknown error"
                    )
                )
                return@reverseGeocodeLocation
            }

            val placemark = placemarks?.firstOrNull() as? CLPlacemark?
            if (placemark != null) {
                val address = buildAddress(placemark)
                continuation.resume(address)
            } else {
                continuation.resume("Address not found")
            }
        }
    }

    private fun buildAddress(placemark: CLPlacemark): String {
        val street = placemark.thoroughfare.orEmpty()
        val city = placemark.locality.orEmpty()
        val state = placemark.administrativeArea.orEmpty()
        val postalCode = placemark.postalCode.orEmpty()
        val country = placemark.country.orEmpty()

        return listOf(street, city, state, postalCode, country)
            .filter { it.isNotEmpty() }
            .joinToString(", ")
    }
}

enum class LocationPermissionStatus {
    RESTRICTED_OR_DENIED,
    NOT_DETERMINED,
    ACCEPTED
}

@OptIn(ExperimentalForeignApi::class)
actual suspend fun getLocation(): Location? =
    IosLocationManager()
        .run {
            if (requestLocationPermission() == LocationPermissionStatus.ACCEPTED)
                requestCurrentLocation()
                    .run {
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
            else null
}
