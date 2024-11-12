package com.jmp.wayback.presentation.app

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.RELEASE} (${Build.MODEL})"
}

actual fun getPlatform(): Platform = AndroidPlatform()