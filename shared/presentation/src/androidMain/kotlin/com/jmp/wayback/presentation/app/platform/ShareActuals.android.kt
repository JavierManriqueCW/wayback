package com.jmp.wayback.presentation.app.platform

import android.content.Context
import android.content.Intent
import com.jmp.wayback.common.ParkingInformation
import com.jmp.wayback.presentation.R
import com.jmp.wayback.presentation.app.utils.Utils.getContentUri
import org.koin.mp.KoinPlatform

actual fun shareParkingInformation(parkingInformation: ParkingInformation) {
    KoinPlatform.getKoin().get<Context>().run {
        startActivity(
            Intent.createChooser(
                getIntent(this, parkingInformation),
                getString(R.string.share_using)
            ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION)
        )
    }
}

private fun getIntent(
    context: Context,
    parkingInformation: ParkingInformation,
): Intent =
    Intent(Intent.ACTION_SEND).apply {
        putExtra(
            Intent.EXTRA_TEXT,
            "${context.getString(R.string.share_location_title)} ${parkingInformation.address}, " +
                    parkingInformation.parkingTime.apply {
                        if (parkingInformation.detail.isNotEmpty()) {
                            plus("    ${context.getString(R.string.share_note_title)} ${parkingInformation.detail}")
                        }
                    }
        )
        parkingInformation.imagePath?.let {
            context.getContentUri(it).let { uri ->
                putExtra(Intent.EXTRA_STREAM, uri)
                type = "image/*"
            }
        } ?: run { type = "text/plain" }
    }
