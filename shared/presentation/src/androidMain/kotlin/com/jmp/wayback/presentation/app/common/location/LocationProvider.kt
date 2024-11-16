package com.jmp.wayback.presentation.app.common.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jmp.wayback.presentation.R
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationProvider {

    private lateinit var context: Context
    private lateinit var activity: ComponentActivity
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permissionCallback: ((Boolean) -> Unit)? = null

    fun init(
        context: Context,
        activity: ComponentActivity,
    ) {
        this.context = context
        this.activity = activity
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)

        requestPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                permissionCallback?.invoke(isGranted)
                permissionCallback = null
            }
    }

    fun checkLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    fun requestLocationPermission(callback: (Boolean) -> Unit) {
        permissionCallback = callback

        when {
            // Permission is already granted
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                callback(true)
            }

            // Check if "Don't ask again" is enabled
            !shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                showSettingsDialog()
            }

            // Request permission
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle(context.getString(R.string.permission_required_title))
            .setMessage(context.getString(R.string.permission_required_message))
            .setPositiveButton(context.getString(R.string.permission_required_positive_action)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                activity.startActivity(intent)
            }
            .setNegativeButton(context.getString(R.string.permission_required_negative_action)) { dialog, _ ->
                dialog.dismiss()
                permissionCallback?.invoke(false)
            }
            .show()
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
}
