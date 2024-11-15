package com.jmp.wayback.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.common.doOnError
import com.jmp.wayback.domain.interactor.ClearParkingInformation
import com.jmp.wayback.domain.interactor.FetchUpdatedParkingInformation
import com.jmp.wayback.domain.interactor.GetParkingState
import com.jmp.wayback.domain.interactor.SaveParkingInformation
import com.jmp.wayback.presentation.app.common.GeneralUiState
import com.jmp.wayback.presentation.app.common.location.getLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

typealias UiState = GeneralUiState<MainUiState>

class MainViewModel(
    private val fetchUpdatedParkingInformation: FetchUpdatedParkingInformation,
    private val getParkingState: GetParkingState,
    private val saveParkingInformation: SaveParkingInformation,
    private val clearParkingInformation: ClearParkingInformation,
    private val uiProvider: MainUiProvider,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> =
        MutableStateFlow(GeneralUiState.Loading())
    val uiState: StateFlow<UiState> = _uiState

    init {
        fetchUpdatedParkingInformation()
        getParkingState()
    }

    private fun updateUiState(uiState: UiState) {
        _uiState.value = uiState
    }

    fun sendIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateDetailIntent -> updateDetail(intent.detail)
            is MainIntent.ParkIntent -> park(intent.detail)
            is MainIntent.StopParking -> stopParking()
        }
    }

    private fun fetchUpdatedParkingInformation() {
        viewModelScope.launch {
            fetchUpdatedParkingInformation.invoke()
        }
    }

    private fun getParkingState() {
        viewModelScope.launch {
            getParkingState
                .invoke()
                .collect { parkingState ->
                    if (parkingState.loaded) {
                        updateUiState(GeneralUiState.Loaded(uiProvider.provide(parkingState.parkingInformation)))
                    } else {
                        updateUiState(GeneralUiState.Loading())
                    }
                }
        }
    }

    private fun updateDetail(detail: String) {
        val state: MainUiState = (_uiState.value as GeneralUiState.Loaded).data
        updateUiState(
            GeneralUiState.Loaded(
                state.copy(nonParkedUiState = state.nonParkedUiState.copy(detailText = detail))
            )
        )
    }

    private fun park(detail: String) {
        viewModelScope.launch {
            getLocation()?.let { location ->
                saveParkingInformation(
                    address = location.address,
                    latitude = location.latitude,
                    longitude = location.longitude,
                    detail = detail
                ).doOnError {
                    println("Error")
                }
            }
        }
    }

    private fun stopParking() {
        viewModelScope.launch {
            clearParkingInformation()
        }
    }
}
