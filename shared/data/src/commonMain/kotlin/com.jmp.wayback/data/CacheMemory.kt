package com.jmp.wayback.data

import com.jmp.wayback.common.ParkingInformation
import com.jmp.wayback.common.ParkingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class CacheMemory {

    private var parkingState = MutableStateFlow(
        ParkingState(
            loaded = false,
            parkingInformation = null
        )
    )

    fun getParkingState(): Flow<ParkingState> = parkingState

    fun updateParkingInformation(parkingInformation: ParkingInformation?) {
        this.parkingState.value = parkingState.value.copy(
            loaded = true,
            parkingInformation = parkingInformation
        )
    }
}
