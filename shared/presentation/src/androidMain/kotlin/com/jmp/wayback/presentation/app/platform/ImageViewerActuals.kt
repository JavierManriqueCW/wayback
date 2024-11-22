package com.jmp.wayback.presentation.app.platform

import android.content.Context
import android.content.Intent
import com.jmp.wayback.presentation.app.utils.Utils.getContentUri
import org.koin.mp.KoinPlatform

actual fun showPicture(picturePath: String) {
    KoinPlatform.getKoin().get<Context>().apply {
        getContentUri(picturePath)?.let { uri ->
            startActivity(
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(uri, "image/*")
                    addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            )
        }
    }
}
