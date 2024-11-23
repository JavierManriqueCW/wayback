package com.jmp.wayback.presentation.app.platform

import androidx.compose.ui.graphics.ImageBitmap
import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.mp.KoinPlatform
import kotlin.coroutines.resume

actual suspend fun checkCameraPermissions(): Boolean =
    KoinPlatform.getKoin().inject<CameraProvider>().value.run {
        suspendCancellableCoroutine { continuation ->
            checkAuthorizationStatus { granted ->
                continuation.resume(granted)
            }
        }
    }

actual suspend fun requestCameraPermissions(callback: (Boolean) -> Unit) {
    KoinPlatform.getKoin().inject<CameraProvider>().value.run {
        requestAuthorization { granted ->
            callback(granted)
        }
    }
}

actual suspend fun takeCameraPicture(callback: (String?) -> Unit) {
    KoinPlatform.getKoin().inject<CameraProvider>().value.run {
        callback(takePicture())
    }
}

actual fun String.toImageBitmap(): ImageBitmap? =
    KoinPlatform.getKoin().inject<CameraProvider>().value.run {
        getUIImageFromFilePath(this@toImageBitmap)?.let { uiImage ->
            uiImageToImageBitmap(uiImage)
        }
    }
