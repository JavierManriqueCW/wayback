package com.jmp.wayback.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.common.doOnError
import com.jmp.wayback.common.doOnSuccess
import com.jmp.wayback.domain.interactor.ClearParkingInformation
import com.jmp.wayback.domain.interactor.FetchUpdatedParkingInformation
import com.jmp.wayback.domain.interactor.GetParkingState
import com.jmp.wayback.domain.interactor.SaveParkingInformation
import com.jmp.wayback.presentation.app.common.state.GeneralUiState
import com.jmp.wayback.presentation.app.platform.checkCameraPermissions
import com.jmp.wayback.presentation.app.platform.checkLocationPermissions
import com.jmp.wayback.presentation.app.platform.getLocation
import com.jmp.wayback.presentation.app.platform.requestCameraPermissions
import com.jmp.wayback.presentation.app.platform.requestLocationPermissions
import com.jmp.wayback.presentation.app.platform.takeCameraPicture
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

    private fun updateUiStateToLoadingParking(loading: Boolean) {
        (_uiState.value as GeneralUiState.Loaded).data.also { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(parking = loading))
                )
            )
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

    private fun updateStopDialogVisibility(visible: Boolean) {
        val state: MainUiState = (_uiState.value as GeneralUiState.Loaded).data
        updateUiState(
            GeneralUiState.Loaded(
                state.copy(
                    parkedUiState = state.parkedUiState?.stopParkingDialogState?.copy(visible = visible)
                    ?.let { state.parkedUiState.copy(stopParkingDialogState = it) })
            )
        )
    }

    fun sendIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateDetailIntent -> updateDetail(intent.detail)
            is MainIntent.TakePicture -> takePicture()
            is MainIntent.RemovePicture -> removePicture()
            is MainIntent.ShowStopParkingDialog -> updateStopDialogVisibility(true)
            is MainIntent.DismissStopParkingDialog -> updateStopDialogVisibility(false)
            is MainIntent.ParkIntent -> park(intent.detail, intent.imagePath)
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

    private fun park(
        detail: String,
        imagePath: String?
    ) {
        updateUiStateToLoadingParking(true)

        fun saveParkingInformation() {
            viewModelScope.launch {
                getLocation()?.let { location ->
                    saveParkingInformation(
                        address = location.address,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        detail = detail,
                        imagePath = imagePath
                    ).doOnSuccess {
                        updateUiStateToLoadingParking(false)
                    }.doOnError {
                        updateUiStateToLoadingParking(false)
                    }
                }
            }
        }

        viewModelScope.launch {
            when (checkLocationPermissions()) {
                true -> saveParkingInformation()
                false -> requestLocationPermissions {
                    if (it) { saveParkingInformation() }
                    else updateUiStateToLoadingParking(false)
                }
            }
        }
    }

    private fun stopParking() {
        viewModelScope.launch {
            clearParkingInformation()
        }
    }

    private fun takePicture() {
        fun takePicture() {
            viewModelScope.launch {
                takeCameraPicture { imagePath ->
                    (_uiState.value as GeneralUiState.Loaded).data.also { state ->
                        updateUiState(
                            GeneralUiState.Loaded(
                                state.copy(
                                    nonParkedUiState = state.nonParkedUiState.copy(
                                        imagePath = imagePath
                                    )
                                )
                            )
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            when (checkCameraPermissions()) {
                true -> takePicture()
                false -> requestCameraPermissions { if (it) { takePicture() } }
            }
        }
    }

    private fun removePicture() {
        (_uiState.value as GeneralUiState.Loaded).data.also { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(imagePath = null))
                )
            )
        }
    }
}
