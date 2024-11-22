package com.jmp.wayback.presentation.app.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

object Utils {

    fun Context.getContentUri(path: String): Uri? =
        FileProvider
            .getUriForFile(
                this,
                "${packageName}.fileprovider",
                File(path)
            )

    fun Context.uriToBitmap(uri: Uri): Bitmap? =
        try {
            contentResolver.openInputStream(uri)?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
}
