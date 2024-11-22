package com.jmp.wayback.common

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

actual fun obtainParkingTime(): String {
    val date = Date(System.currentTimeMillis())
    val dateFormat = SimpleDateFormat("MMMM dd, EEEE - HH:mm", Locale.getDefault())
    return dateFormat.format(date)
}
