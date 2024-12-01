package com.jmp.wayback.presentation.app.platform

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openMap(latitude: Double, longitude: Double) {
    val urlString = "http://maps.apple.com/?ll=${latitude},${longitude}&q=${"Wayback pin"}"
    val url = NSURL.URLWithString(urlString) ?: return

    if (UIApplication.sharedApplication.canOpenURL(url)) {
        UIApplication.sharedApplication.openURL(
            url = url,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}
