package com.jmp.wayback.presentation.app.provider.camera

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.jmp.wayback.presentation.R
import com.jmp.wayback.presentation.app.utils.Utils.getContentUri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull

class CameraProvider {

    private lateinit var context: Context
    private lateinit var activity: ComponentActivity
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var permissionCallback: ((Boolean) -> Unit)? = null
    private val cameraFlow = MutableStateFlow<String?>(null)
    var isActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val imageCapture = ImageCapture.Builder().build().apply {
        setTargetRotation(Surface.ROTATION_0)
    }

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
    }

    fun updateCameraFlow(camera: String?) {
        cameraFlow.value = camera
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

    fun startCamera(
        context: Context,
        lifecycleOwner: LifecycleOwner,
        previewView: PreviewView,
        imageCapture: ImageCapture
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val preview = Preview.Builder()
                .build()
                .also {
                    it.surfaceProvider = previewView.surfaceProvider
                }

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )

            } catch(exc: Exception) {
                println("Use case binding failed: ${exc.message}")
            }

        }, ContextCompat.getMainExecutor(context))
    }

    suspend fun takePicture(callback: (String?) -> Unit) {
        isActive.value = true
        callback(cameraFlow.drop(1).firstOrNull())
    }

    fun captureCameraPicture(
        context: Context,
        imageCapture: ImageCapture,
        onImageCaptured: (Uri) -> Unit,
        onError: (ImageCaptureException) -> Unit
    ) {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                context.contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            ).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    onError(exc)
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    output.savedUri?.let {
                        onImageCaptured(it)
                    } ?: run {
                        onError(
                            ImageCaptureException(
                                ImageCapture.ERROR_UNKNOWN,
                                "Photo capture failed: null uri",
                                null
                            )
                        )
                    }
                }
            }
        )
    }

    fun deleteFile(context: Context, filePath: String) {
        try {
            val rowsDeleted = context.contentResolver.delete(
                getContentUri(filePath),
                null,
                null
            )
            if (rowsDeleted > 0) {
                println("File deleted successfully.")
            } else {
                println("Failed to delete the file.")
            }
        } catch (e: Exception) {
            println("Error deleting file: ${e.message}")
            e.printStackTrace()
        }
    }

    fun releaseCamera() {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()
        cameraProvider.unbindAll()
        isActive.value = false
    }
}
