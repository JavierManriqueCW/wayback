package com.jmp.wayback.domain.interactor

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.domain.repository.Repository

class ShouldShowOnboarding(
    private val repository: Repository
) {

    fun execute(): Either<Failure, Boolean> =
        repository.shouldShowOnboarding()
}
