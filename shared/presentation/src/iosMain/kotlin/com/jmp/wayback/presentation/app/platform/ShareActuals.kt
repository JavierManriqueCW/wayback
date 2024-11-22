package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.common.ParkingInformation
import platform.UIKit.UIActivityTypePostToFacebook
import platform.UIKit.UIActivityTypePostToTwitter
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication
import platform.UIKit.UIImage

actual fun shareParkingInformation(parkingInformation: ParkingInformation) {
    val currentController = UIApplication.sharedApplication.keyWindow?.rootViewController

    val image = parkingInformation.imagePath?.let { imagePath ->
        UIImage.imageWithContentsOfFile(imagePath)
    }

    val itemsToShare = mutableListOf<Any>(
        "Parked at ${parkingInformation.address}, ",
        parkingInformation.parkingTime,

    )

    if (parkingInformation.detail.isNotEmpty()) {
        itemsToShare.add("    ")
        itemsToShare.add("Note: ${parkingInformation.detail}")
    }

    if (image != null) {
        itemsToShare.add(image)
    }

    val activityViewController = UIActivityViewController(
        activityItems = itemsToShare,
        applicationActivities = null
    )

    activityViewController.excludedActivityTypes = listOf(
        UIActivityTypePostToFacebook,
        UIActivityTypePostToTwitter
    )

    currentController?.presentViewController(activityViewController, animated = true, completion = null)
}
