package com.jmp.wayback.presentation.app.platform

import platform.Foundation.NSURL
import platform.QuickLook.QLPreviewController
import platform.QuickLook.QLPreviewControllerDataSourceProtocol
import platform.QuickLook.QLPreviewItemProtocol
import platform.UIKit.UIApplication
import platform.darwin.NSObject

actual fun showPicture(picturePath: String) {
    val viewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val previewController = QLPreviewController()
    previewController.dataSource = object : NSObject(), QLPreviewControllerDataSourceProtocol {
        override fun numberOfPreviewItemsInPreviewController(controller: QLPreviewController): Long {
            return 1
        }

        override fun previewController(controller: QLPreviewController, previewItemAtIndex: Long): QLPreviewItemProtocol =
            object : NSObject(), QLPreviewItemProtocol {
                override fun previewItemURL(): NSURL = NSURL.fileURLWithPath(picturePath)
            }
    }
    viewController?.presentViewController(previewController, animated = true, completion = null)
}
