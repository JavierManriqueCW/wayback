package com.jmp.wayback.presentation.app.platform

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun checkCameraPermissions(): Boolean

expect suspend fun requestCameraPermissions(callback: (Boolean) -> Unit)

expect suspend fun takeCameraPicture(callback: (String?) -> Unit)

expect fun String.toImageBitmap(): ImageBitmap?
