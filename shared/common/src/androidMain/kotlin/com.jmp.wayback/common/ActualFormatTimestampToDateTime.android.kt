package com.jmp.wayback.common

import java.text.SimpleDateFormat
import java.util.*

actual fun obtainParkingTime(): String {
    val date = Date(System.currentTimeMillis())
    val dateFormat = SimpleDateFormat("MMMM yyyy, EEEE dd\nHH:mm", Locale.getDefault())
    return dateFormat.format(date)
}
