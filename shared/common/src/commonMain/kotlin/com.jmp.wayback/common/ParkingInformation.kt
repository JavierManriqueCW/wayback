package com.jmp.wayback.common

data class ParkingInformation(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val detail: String = "",
    val parkingTime: String
)
