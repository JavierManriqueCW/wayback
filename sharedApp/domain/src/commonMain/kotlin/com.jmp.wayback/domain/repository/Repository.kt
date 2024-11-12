package com.jmp.wayback.domain.repository

import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure

interface Repository {

    fun disableOnboarding(): Either<Failure, Unit>
    fun shouldShowOnboarding(): Either<Failure, Boolean>
}
