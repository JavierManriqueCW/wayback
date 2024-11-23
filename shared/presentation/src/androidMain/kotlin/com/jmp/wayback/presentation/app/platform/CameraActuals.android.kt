package com.jmp.wayback.presentation.app.platform

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import com.jmp.wayback.presentation.app.utils.Utils.getContentUri
import com.jmp.wayback.presentation.app.utils.Utils.uriToBitmap
import org.koin.mp.KoinPlatform

actual suspend fun checkCameraPermissions(): Boolean =
    KoinPlatform.getKoin().get<CameraProvider>().checkCameraPermission()

actual suspend fun requestCameraPermissions(callback: (Boolean) -> Unit) =
    KoinPlatform.getKoin().get<CameraProvider>().requestCameraPermission(callback)

actual suspend fun takeCameraPicture(callback: (String?) -> Unit) {
    KoinPlatform.getKoin().get<CameraProvider>().takePicture(callback)
}

actual fun String.toImageBitmap(): ImageBitmap? =
    KoinPlatform.getKoin().get<Context>().run {
        getContentUri(this@toImageBitmap)?.let { uri ->
            uriToBitmap(uri)?.asImageBitmap()
        }
    }
