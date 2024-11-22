package com.jmp.wayback.presentation.app.platform

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import org.koin.mp.KoinPlatform

actual fun openMap(latitude: Double, longitude: Double) {
    KoinPlatform.getKoin().get<Context>().also { context ->
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("geo:0,0?q=$latitude,$longitude(Wayback pin)")
        ).run {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            try {
                context.startActivity(this)
            } catch (e: Exception) {
                Toast.makeText(context, "No maps application found", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
