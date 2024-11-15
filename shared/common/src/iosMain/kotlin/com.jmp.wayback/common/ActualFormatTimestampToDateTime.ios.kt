package com.jmp.wayback.common

import platform.Foundation.*

actual fun obtainParkingTime(): String {
    val timestamp = (NSDate().timeIntervalSince1970 * 1000).toLong()
    val date = NSDate.dateWithTimeIntervalSince1970(timestamp / 1000.0)
    val formatter = NSDateFormatter().apply {
        dateFormat = "MMMM yyyy, EEEE dd\nHH:mm"
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}
