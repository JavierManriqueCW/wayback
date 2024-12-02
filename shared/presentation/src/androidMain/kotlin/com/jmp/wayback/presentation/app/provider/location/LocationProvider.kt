package com.jmp.wayback.presentation.app.provider.location

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.jmp.wayback.presentation.R
import com.jmp.wayback.presentation.app.platform.Location
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume

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
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                callback(true)
            }

            shouldShowRequestPermissionRationale(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                callback(false)
                showSettingsDialog()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle(context.getString(R.string.permission_required_title))
            .setMessage(context.getString(R.string.location_permission_required_message))
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
    suspend fun getUserLocation(): Location? =
        suspendCancellableCoroutine { continuation ->
            fusedLocationClient.getCurrentLocation(
                CurrentLocationRequest.Builder().build(),
                null
            ).addOnSuccessListener { location ->
                    if (location != null) {
                        getAddressFromLatLong(
                            context = context,
                            latitude = location.latitude,
                            longitude = location.longitude
                        ) { address ->
                            continuation.resume(
                                Location(
                                    address = address,
                                    latitude = location.latitude,
                                    longitude = location.longitude
                                )
                            )
                        }
                    } else {
                        continuation.resume(null)
                    }
                }
                .addOnFailureListener {
                    continuation.resume(null)
                }
        }

    private fun getAddressFromLatLong(
        context: Context,
        latitude: Double,
        longitude: Double,
        callback: (String) -> Unit
    ) {
        val geocoder = Geocoder(context, Locale.getDefault())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    if (addresses.isNotEmpty()) {
                        val address = addresses[0]
                        callback(address.getAddressLine(0))
                    } else {
                        callback("Address not found")
                    }
                }

                override fun onError(errorMessage: String?) {
                    callback("Address not found")
                }
            })
        } else {
            // Fallback for older Android versions
            @Suppress("DEPRECATION")
            try {
                val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    val address: Address = addresses[0]
                    callback(address.getAddressLine(0))
                } else {
                    callback("Address not found")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                callback("Address not found")
            }
        }
    }
}
