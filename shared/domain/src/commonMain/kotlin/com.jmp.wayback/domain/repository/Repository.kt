package com.jmp.wayback.domain.repository

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.common.ParkingInformation
import com.jmp.wayback.common.ParkingState
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun disableOnboarding()
    fun shouldShowOnboarding(): Flow<Boolean>
    fun getParkingState(): Flow<ParkingState>
    suspend fun clearParkingInformation()
    suspend fun fetchUpdatedParkingInformation()
    suspend fun saveParkingInformation(parkingInformation: ParkingInformation) : Either<Failure, Unit>
}
