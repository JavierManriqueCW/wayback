package com.jmp.wayback.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jmp.wayback.common.Either
import com.jmp.wayback.common.Failure
import com.jmp.wayback.common.ParkingInformation
import com.jmp.wayback.common.ParkingState
import com.jmp.wayback.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class RepositoryImplementation(
    private val dataStore: DataStore<Preferences>,
    private val cacheMemory: CacheMemory
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

    override suspend fun fetchUpdatedParkingInformation() {
        dataStore.data.collect { preferences ->
            val address = preferences[ADDRESS]
            val latitude = preferences[LATITUDE]
            val longitude = preferences[LONGITUDE]
            val detail = preferences[DETAIL]
            val parkingTime = preferences[PARKING_TIME]

            if (
                address != null &&
                latitude != null &&
                longitude != null &&
                parkingTime != null
            ) {
                cacheMemory.updateParkingInformation(
                    ParkingInformation(
                        address,
                        latitude,
                        longitude,
                        detail.orEmpty(),
                        parkingTime
                    )
                )
            } else {
                cacheMemory.updateParkingInformation(null)
            }
        }
    }

    override fun getParkingState(): Flow<ParkingState> =
        cacheMemory.getParkingState()

    override suspend fun saveParkingInformation(
        parkingInformation: ParkingInformation
    ): Either<Failure, Unit> =
        try {
            dataStore.edit { preferences ->
                preferences[ADDRESS] = parkingInformation.address
                preferences[LATITUDE] = parkingInformation.latitude
                preferences[LONGITUDE] = parkingInformation.longitude
                preferences[DETAIL] = parkingInformation.detail
                preferences[PARKING_TIME] = parkingInformation.parkingTime
            }
            Either.Success(Unit)
        } catch (e: Exception) {
            Either.Error(Failure.ErrorSavingParkingInformation)
        }

    override suspend fun clearParkingInformation() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
        disableOnboarding()
    }

    companion object {
        private val SHOULD_SHOW_ONBOARDING = booleanPreferencesKey("ShouldShowOnboarding")
        private val ADDRESS = stringPreferencesKey("Address")
        private val LATITUDE = doublePreferencesKey("Latitude")
        private val LONGITUDE = doublePreferencesKey("Longitude")
        private val DETAIL = stringPreferencesKey("Detail")
        private val PARKING_TIME = stringPreferencesKey("ParkingTime")
    }
}
