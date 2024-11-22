package com.jmp.wayback.common

import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.dateWithTimeIntervalSince1970
import platform.Foundation.timeIntervalSince1970

actual fun obtainParkingTime(): String {
    val timestamp = (NSDate().timeIntervalSince1970 * 1000).toLong()
    val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
    val formatter = NSDateFormatter().apply {
        dateFormat = "MMMM dd, EEEE - HH:mm"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}
