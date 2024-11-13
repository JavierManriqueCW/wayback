package com.jmp.wayback.domain.interactor

import com.jmp.wayback.domain.repository.Repository
import kotlinx.coroutines.flow.firstOrNull

class ShouldShowOnboarding(
    private val repository: Repository
) {

    suspend operator fun invoke(): Boolean =
        repository.shouldShowOnboarding().firstOrNull() ?: true
}
