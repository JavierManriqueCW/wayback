package com.jmp.wayback.domain.interactor

import com.jmp.wayback.domain.repository.Repository

class ClearParkingInformation(
    private val repository: Repository
) {

    suspend operator fun invoke() {
        repository.clearParkingInformation()
    }
}
