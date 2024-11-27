package com.jmp.wayback.presentation.app.provider.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import com.jmp.wayback.presentation.app.util.localized
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.Image
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVAuthorizationStatusDenied
import platform.AVFoundation.AVAuthorizationStatusRestricted
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleCancel
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationOpenSettingsURLString
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerSourceType
import platform.UIKit.UIModalPresentationCurrentContext
import kotlin.coroutines.resume

class CameraProvider {

    private val picker = UIImagePickerController()
    private val cameraManager = IosCameraManager()

    init {
        picker.sourceType = UIImagePickerControllerSourceType.UIImagePickerControllerSourceTypeCamera
        picker.modalPresentationStyle = UIModalPresentationCurrentContext
        picker.delegate = cameraManager
    }

    fun checkAuthorizationStatus(callback: (Boolean) -> Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        when (status) {
            AVAuthorizationStatusAuthorized -> callback(true)
            else -> callback(false)
        }
    }

    fun requestAuthorization(callback: (Boolean) -> Unit) {
        val status = AVCaptureDevice.authorizationStatusForMediaType(AVMediaTypeVideo)
        when (status) {
            AVAuthorizationStatusAuthorized -> callback(true)
            AVAuthorizationStatusDenied, AVAuthorizationStatusRestricted -> showSettingsAlert()
            else -> {
                AVCaptureDevice.requestAccessForMediaType(AVMediaTypeVideo) { granted ->
                    callback(granted)
                }
            }
        }
    }

    suspend fun takePicture(): String? =
        suspendCancellableCoroutine { cont ->
            cameraManager.setContinuation { data -> cont.resume(data) }
            cameraManager.presentViewController(picker)
        }

    fun getUIImageFromFilePath(filePath: String): UIImage? =
        if (doesFileExist(filePath)) {
            UIImage(contentsOfFile = filePath)
        } else {
            null
        }

    private fun doesFileExist(filePath: String): Boolean {
        val fileManager = NSFileManager.defaultManager
        return fileManager.fileExistsAtPath(filePath)
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.toByteArray(): ByteArray? {
            val imageData = UIImageJPEGRepresentation(this, COMPRESSION_QUALITY) ?: return null
            val bytes = imageData.bytes ?: return null
            val length = imageData.length

            val data: CPointer<ByteVar> = bytes.reinterpret()
            return ByteArray(length.toInt()) { index -> data[index] }
    }

    fun uiImageToImageBitmap(uiImage: UIImage): ImageBitmap? {
        val byteArray = uiImage.toByteArray()
        return if (byteArray != null) {
            Image.makeFromEncoded(byteArray).toComposeImageBitmap()
        } else {
            null
        }
    }

    private fun showSettingsAlert() {
        val alert = UIAlertController.alertControllerWithTitle(
            title = permissionRequiredTitle,
            message = cameraPermissionRequiredMessage,
            preferredStyle = UIAlertControllerStyleAlert
        )

        alert.addAction(
            UIAlertAction.actionWithTitle(
                title = permissionRequiredPositiveAction,
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
                title = permissionRequiredNegativeAction,
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

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
        val permissionRequiredTitle = "permission_required_title".localized()
        val cameraPermissionRequiredMessage = "camera_permission_required_message".localized()
        val permissionRequiredPositiveAction = "permission_required_positive_action".localized()
        val permissionRequiredNegativeAction = "permission_required_negative_action".localized()
    }
}
