package com.jmp.wayback.presentation.app.provider.camera

import platform.Foundation.NSDate
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask
import platform.Foundation.timeIntervalSince1970
import platform.Foundation.writeToFile
import platform.UIKit.UIApplication
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIImagePickerController
import platform.UIKit.UIImagePickerControllerDelegateProtocol
import platform.UIKit.UIImagePickerControllerOriginalImage
import platform.UIKit.UINavigationControllerDelegateProtocol
import platform.UIKit.UIViewController
import platform.darwin.NSObject

class IosCameraManager : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    private var continuation: ((String) -> Unit)? = null

    fun setContinuation(continuation: (String) -> Unit) {
        this.continuation = continuation
    }

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        (didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage] as? UIImage)?.let { picture ->
            saveImageToFile(picture)?.let { picturePath ->
                continuation?.invoke(picturePath)
            }
        }
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, completion = null)
    }

    fun presentViewController(picker: UIImagePickerController) {
        getCurrentViewController()?.presentViewController(
            viewControllerToPresent = picker,
            animated = true,
            completion = null
        )
    }

    private fun getCurrentViewController(): UIViewController? {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        return rootViewController?.presentedViewController ?: rootViewController
    }

    private fun saveImageToFile(image: UIImage): String? {
        val documentsDirectory = NSSearchPathForDirectoriesInDomains(
            directory = NSDocumentDirectory,
            domainMask = NSUserDomainMask,
            expandTilde = true
        ).firstOrNull() as? String

        if (documentsDirectory != null) {
            val fileName = "captured_image_${NSDate().timeIntervalSince1970}.jpg"
            val filePath = "$documentsDirectory/$fileName"

            val imageData = UIImageJPEGRepresentation(image = image, compressionQuality = 0.8)
            if (imageData != null && imageData.writeToFile(filePath, atomically = true)) {
                return filePath
            }
        }
        return null
    }
}
