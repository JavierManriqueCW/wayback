package com.jmp.wayback.data

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.domain.repository.Repository

class RepositoryImplementation() : Repository {

    override fun disableOnboarding(): Either<Failure, Unit> {
        return Either.Success(Unit)
    }

    override fun shouldShowOnboarding(): Either<Failure, Boolean> {
        return Either.Success(true)
    }
}
