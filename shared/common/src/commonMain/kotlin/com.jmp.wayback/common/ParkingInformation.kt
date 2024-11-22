package com.jmp.wayback.common

data class ParkingInformation(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val detail: String = String(),
    val imagePath: String? = null,
    val parkingTime: String
)
