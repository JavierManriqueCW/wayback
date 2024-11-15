package com.jmp.wayback.domain.interactor

import com.jmp.wayback.common.ParkingState
import com.jmp.wayback.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetParkingState(
    private val repository: Repository
) {

    operator fun invoke(): Flow<ParkingState> =
        repository.getParkingState()
}
