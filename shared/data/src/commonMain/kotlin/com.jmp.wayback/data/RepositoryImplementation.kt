package com.jmp.wayback.data

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RepositoryImplementation(
    private val dataStore: DataStore<Preferences>
) : Repository {

    override suspend fun disableOnboarding() {
        dataStore.edit { preferences ->
            preferences[SHOULD_SHOW_ONBOARDING] = false
        }
    }

    override fun shouldShowOnboarding(): Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[SHOULD_SHOW_ONBOARDING] ?: true
        }

    companion object {
        private val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey("ShouldShowOnboarding")
    }
}
