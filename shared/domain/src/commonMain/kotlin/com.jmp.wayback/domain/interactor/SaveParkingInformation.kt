package com.jmp.wayback.domain.interactor

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.common.ParkingInformation
import com.jmp.wayback.common.obtainParkingTime
import com.jmp.wayback.domain.repository.Repository

class SaveParkingInformation(
    private val repository: Repository
) {

    suspend operator fun invoke(
        address: String,
        latitude: Double,
        longitude: Double,
        detail: String,
        imagePath: String?
    ): Either<Failure, Unit> =
        repository.saveParkingInformation(
            ParkingInformation(
                address = address,
                latitude = latitude,
                longitude = longitude,
                detail = detail,
                imagePath = imagePath,
                parkingTime = obtainParkingTime()
            )
        )
}
