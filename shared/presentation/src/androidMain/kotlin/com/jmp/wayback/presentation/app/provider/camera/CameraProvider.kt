package com.jmp.wayback.presentation.app.provider.camera

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import com.jmp.wayback.presentation.R
import com.jmp.wayback.presentation.app.utils.Utils.getContentUri
import com.jmp.wayback.presentation.app.utils.Utils.uriToBitmap
import java.io.File

class CameraProvider {

    private lateinit var context: Context
    private lateinit var activity: ComponentActivity
    private lateinit var picturePath: String
    private lateinit var takePictureLauncher: ActivityResultLauncher<Uri>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permissionCallback: ((Boolean) -> Unit)? = null
    private var takePictureCallback: ((String?) -> Unit)? = null

    fun init(
        context: Context,
        activity: ComponentActivity,
    ) {
        this.context = context
        this.activity = activity

        requestPermissionLauncher =
            activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                permissionCallback?.invoke(isGranted)
                permissionCallback = null
            }

        takePictureLauncher =
            activity.registerForActivityResult(ActivityResultContracts.TakePicture()) {
                context.apply {
                    getContentUri(picturePath)?.let { uri ->
                        uriToBitmap(uri)?.asImageBitmap()?.let {
                            takePictureCallback?.invoke(picturePath)
                        }
                    }
                }
            }
    }

    fun checkCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    fun requestCameraPermission(callback: (Boolean) -> Unit) {
        permissionCallback = callback

        when {
            (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) -> {
                callback(true)
            }

            shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA) -> {
                showSettingsDialog()
            }

            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showSettingsDialog() {
        AlertDialog.Builder(activity)
            .setTitle(context.getString(R.string.permission_required_title))
            .setMessage(context.getString(R.string.camera_permission_required_message))
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

    fun takePicture(callback: (String?) -> Unit) {
        takePictureCallback = callback
        picturePath = "${context.filesDir}/${System.currentTimeMillis()}.jpg"
        takePictureLauncher.launch(context.getContentUri(picturePath))
    }

    fun deleteFile(filePath: String) {
        File(filePath).let { file ->
            if (file.exists()) {
                val isDeleted = file.delete()
                if (isDeleted) {
                    println("File deleted successfully.")
                } else {
                    println("Failed to delete the file.")
                }
                isDeleted
            } else {
                println("File not found at: $filePath")
                false
            }
        }
    }
}
