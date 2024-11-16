package com.jmp.wayback.presentation.app.common.location

import kotlinx.coroutines.suspendCancellableCoroutine
import platform.CoreLocation.CLGeocoder
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLPlacemark
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.CoreLocation.kCLLocationAccuracyBest
import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationProvider {

    private val locationManager = CLLocationManager()
    private val iosLocationManager = IosLocationManager()
    private var callback: ((Boolean) -> Unit)? = null

    init {
        locationManager.delegate = iosLocationManager
        locationManager.desiredAccuracy = kCLLocationAccuracyBest
    }

    suspend fun requestCurrentLocation(): Result<CLLocation> =
        suspendCancellableCoroutine { continuation ->
            iosLocationManager.setLocationResultContinuation(continuation)
            locationManager.requestLocation()
        }

    fun checkAuthorizationStatus(): LocationPermissionStatus =
        iosLocationManager.checkAuthorizationStatus()

    suspend fun requestLocationPermission(callback: (Boolean) -> Unit) {
        suspendCancellableCoroutine { continuation ->
            iosLocationManager.setLocationResultContinuation(continuation)
            iosLocationManager.setCallback(callback)
            when (CLLocationManager.authorizationStatus()) {
                kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                    callback(true)
                }

                kCLAuthorizationStatusDenied, kCLAuthorizationStatusRestricted -> {
                    showSettingsAlert()
                }

                else -> {
                    this.callback = callback
                    locationManager.requestWhenInUseAuthorization()
                }
            }
        }
    }

    private fun showSettingsAlert() {
        val alert = UIAlertController.alertControllerWithTitle(
            title = "Location Permission Required",
            message = "Location access is required for this feature. Please enable it in the app settings.",
            preferredStyle = UIAlertControllerStyleAlert
        )

        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "Go to Settings",
                style = UIAlertActionStyleDefault
            ) {
                val settingsUrl = NSURL(string = UIApplicationOpenSettingsURLString)
                UIApplication.sharedApplication.openURL(
                    settingsUrl,
                    options = emptyMap<Any?, Any>(),
                    completionHandler = {}
                )
            }
        )

        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = "Cancel",
                style = UIAlertActionStyleCancel
            ) {
                alert.dismissViewControllerAnimated(true, null)
            }
        )

        UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
            alert,
            animated = true,
            completion = null
        )
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
