package com.jmp.wayback.domain.repository

import kotlinx.coroutines.flow.Flow

interface Repository {

    suspend fun disableOnboarding()
    fun shouldShowOnboarding(): Flow<Boolean>
}
