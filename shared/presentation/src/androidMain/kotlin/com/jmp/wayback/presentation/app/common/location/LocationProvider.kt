package com.jmp.wayback.presentation.app.common.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationProvider {

    private lateinit var context: Context
    private lateinit var activity: Activity
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    fun init(context: Context, activity: Activity) {
        this.context = context
        this.activity = activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    fun checkAndRequestLocationPermission(): Boolean {
        val fineLocationPermission = Manifest.permission.ACCESS_FINE_LOCATION

        return if (ContextCompat.checkSelfPermission(context, fineLocationPermission) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(fineLocationPermission),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            false
        }
    }

    @SuppressLint("MissingPermission")
    suspend fun getUserLocation(): Location =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        continuation.resume(
                            Location(
                                address = getAddressFromLatLong(
                                    location.latitude,
                                    location.longitude
                                ),
                                latitude = location.latitude,
                                longitude = location.longitude
                            )
                        )
                    } else {
                        continuation.resumeWithException(IllegalStateException("Location is null"))
                    }
                }
                .addOnFailureListener { exception ->
                    continuation.resumeWithException(exception)
                }
        }

    private fun getAddressFromLatLong(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            val addresses: List<Address>? =
                geocoder.getFromLocation(latitude, longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                address.getAddressLine(0)
            } else {
                "Address not found"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Error: Unable to get address"
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
