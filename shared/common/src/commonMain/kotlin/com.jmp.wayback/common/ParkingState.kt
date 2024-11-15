package com.jmp.wayback.common

data class ParkingState(
    val loaded: Boolean = false,
    val parkingInformation: ParkingInformation? = null
)
