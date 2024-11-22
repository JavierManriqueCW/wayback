package com.jmp.wayback.presentation.app.provider.camera

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.get
import kotlinx.cinterop.reinterpret
import kotlinx.coroutines.suspendCancellableCoroutine
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import platform.AVFoundation.AVAuthorizationStatusAuthorized
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.authorizationStatusForMediaType
import platform.AVFoundation.requestAccessForMediaType
import platform.CoreFoundation.CFDataGetBytePtr
import platform.CoreFoundation.CFDataGetLength
import platform.CoreFoundation.CFRelease
import platform.CoreGraphics.CGDataProviderCopyData
import platform.CoreGraphics.CGImageAlphaInfo
import platform.CoreGraphics.CGImageGetAlphaInfo
import platform.CoreGraphics.CGImageGetBytesPerRow
import platform.CoreGraphics.CGImageGetDataProvider
import platform.CoreGraphics.CGImageGetHeight
import platform.CoreGraphics.CGImageGetWidth
import platform.CoreGraphics.CGImageRelease
import platform.Foundation.NSFileManager
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
    fun uiImageToSkiaImage(uiImage: UIImage): Image? {
        val imageRef = uiImage.CGImage ?: return null

        val width = CGImageGetWidth(imageRef).toInt()
        val height = CGImageGetHeight(imageRef).toInt()

        val bytesPerRow = CGImageGetBytesPerRow(imageRef)
        val data = CGDataProviderCopyData(CGImageGetDataProvider(imageRef))
        val bytePointer = CFDataGetBytePtr(data)
        val length = CFDataGetLength(data)

        val alphaType = when (CGImageGetAlphaInfo(imageRef)) {
            CGImageAlphaInfo.kCGImageAlphaPremultipliedFirst,
            CGImageAlphaInfo.kCGImageAlphaPremultipliedLast -> ColorAlphaType.PREMUL
            CGImageAlphaInfo.kCGImageAlphaFirst,
            CGImageAlphaInfo.kCGImageAlphaLast -> ColorAlphaType.UNPREMUL
            CGImageAlphaInfo.kCGImageAlphaNone,
            CGImageAlphaInfo.kCGImageAlphaNoneSkipFirst,
            CGImageAlphaInfo.kCGImageAlphaNoneSkipLast -> ColorAlphaType.OPAQUE
            else -> ColorAlphaType.UNKNOWN
        }

        val byteArray = ByteArray(length.toInt()) { index ->
            bytePointer!![index].toByte()
        }

        CFRelease(data)
        CGImageRelease(imageRef)

        val skiaColorSpace = ColorSpace.sRGB
        val colorType = ColorType.RGBA_8888

        // Convert RGBA to BGRA
        for (i in byteArray.indices step 4) {
            val r = byteArray[i]
            val g = byteArray[i + 1]
            val b = byteArray[i + 2]
            val a = byteArray[i + 3]

            byteArray[i] = b
            byteArray[i + 2] = r
        }

        return Image.makeRaster(
            imageInfo = ImageInfo(
                width = width,
                height = height,
                colorType = colorType,
                alphaType = alphaType,
                colorSpace = skiaColorSpace
            ),
            bytes = byteArray,
            rowBytes = bytesPerRow.toInt(),
        )
    }


    @OptIn(ExperimentalForeignApi::class)
    private fun UIImage.toByteArray(): ByteArray? {
            val imageData = UIImageJPEGRepresentation(this, COMPRESSION_QUALITY) ?: return null
            val bytes = imageData.bytes ?: throw IllegalArgumentException("image bytes is null")
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

    private companion object {
        const val COMPRESSION_QUALITY = 0.99
    }
}
