package com.jmp.wayback.domain.interactor

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.domain.repository.Repository

class DisableOnboarding(
    private val repository: Repository
) {

    operator fun invoke(): Either<Failure, Unit> =
        repository.disableOnboarding()
}
