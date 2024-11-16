package com.jmp.wayback.presentation.app.common.location

import kotlinx.coroutines.CancellableContinuation
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.Foundation.NSError
import platform.darwin.NSObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class IosLocationManager : NSObject(), CLLocationManagerDelegateProtocol {

    private var locationResultContinuation: (CancellableContinuation<Result<CLLocation>>)? = null
    private var callback: ((Boolean) -> Unit)? = null

    fun setCallback(callback: (Boolean) -> Unit) {
        this.callback = callback
    }

    fun setLocationResultContinuation(continuation: CancellableContinuation<Result<CLLocation>>) {
        locationResultContinuation = continuation
    }

    fun checkAuthorizationStatus(): LocationPermissionStatus =
        when (CLLocationManager.authorizationStatus()) {
            kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusAuthorizedAlways -> {
                LocationPermissionStatus.ACCEPTED
            }

            else -> {
                LocationPermissionStatus.RESTRICTED_OR_DENIED
            }
        }

    override fun locationManagerDidChangeAuthorization(manager: CLLocationManager) {
        if (checkAuthorizationStatus() == LocationPermissionStatus.ACCEPTED) {
            callback?.invoke(true)
        }
    }

    override fun locationManager(
        manager: CLLocationManager,
        didChangeAuthorizationStatus: CLAuthorizationStatus
    ) {
        when (didChangeAuthorizationStatus) {
            kCLAuthorizationStatusRestricted,
            kCLAuthorizationStatusDenied -> callback?.invoke(false)

            kCLAuthorizationStatusAuthorizedAlways,
            kCLAuthorizationStatusAuthorizedWhenInUse -> {
                callback?.invoke(true)
            }
        }
    }

    override fun locationManager(manager: CLLocationManager, didFailWithError: NSError) {
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
}
