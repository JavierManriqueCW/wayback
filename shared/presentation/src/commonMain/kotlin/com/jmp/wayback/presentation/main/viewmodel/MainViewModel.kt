package com.jmp.wayback.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.common.Failure
import com.jmp.wayback.common.doOnError
import com.jmp.wayback.common.doOnSuccess
import com.jmp.wayback.domain.interactor.ClearParkingInformation
import com.jmp.wayback.domain.interactor.FetchUpdatedParkingInformation
import com.jmp.wayback.domain.interactor.GetParkingState
import com.jmp.wayback.domain.interactor.SaveParkingInformation
import com.jmp.wayback.presentation.app.common.state.GeneralUiState
import com.jmp.wayback.presentation.app.platform.checkCameraPermissions
import com.jmp.wayback.presentation.app.platform.checkLocationPermissions
import com.jmp.wayback.presentation.app.platform.deleteCameraPicture
import com.jmp.wayback.presentation.app.platform.getLocation
import com.jmp.wayback.presentation.app.platform.requestCameraPermissions
import com.jmp.wayback.presentation.app.platform.requestLocationPermissions
import com.jmp.wayback.presentation.app.platform.takeCameraPicture
import com.jmp.wayback.presentation.app.platform.updatePlatformSpecificIsParkedStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.location_not_found
import wayback.shared.presentation.generated.resources.parking_error
import kotlin.coroutines.resume

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
        _uiState.asLoaded()?.data?.also { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(parking = loading))
                )
            )
        }
    }

    private fun updateDetail(detail: String) {
        _uiState.asLoaded()?.data?.let { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(detailText = detail))
                )
            )
        }
    }

    private fun updateStopDialogVisibility(visible: Boolean) {
        _uiState.asLoaded()?.data?.let { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(
                        parkedUiState = state.parkedUiState?.stopParkingDialogState?.copy(visible = visible)
                            ?.let { state.parkedUiState.copy(stopParkingDialogState = it) })
                )
            )
        }
    }

    private fun dismissParkingErrorAlert() {
        _uiState.asLoaded()?.data?.let { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(error = null))
                )
            )
        }
    }

    fun sendIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateDetailIntent -> updateDetail(intent.detail)
            is MainIntent.TakePicture -> takePicture()
            is MainIntent.RemovePicture -> removePicture()
            is MainIntent.ShowStopParkingDialog -> updateStopDialogVisibility(true)
            is MainIntent.DismissStopParkingDialog -> updateStopDialogVisibility(false)
            is MainIntent.DismissParkingErrorAlert -> dismissParkingErrorAlert()
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
        fun saveParkingInformation(image: String) {
            updateUiStateToLoadingParking(true)

            viewModelScope.launch {
                getLocation()?.let { location ->
                    saveParkingInformation(
                        address = location.address,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        detail = detail,
                        imagePath = image
                    ).doOnSuccess {
                        updatePlatformSpecificIsParkedStatus(isParked = true)
                        updateUiStateToLoadingParking(false)
                    }.doOnError {
                        handleParkingError(it)
                    }
                } ?: run {
                    handleParkingError()
                }
            }
        }

        viewModelScope.launch {
            val image = suspendCancellableCoroutine { continuation ->
                imagePath?.let {
                    continuation.resume(imagePath)
                } ?: run {
                    takePicture { continuation.resume(it) }
                }
            }

            image?.let {
                when (checkLocationPermissions()) {
                    true -> saveParkingInformation(image)
                    false -> requestLocationPermissions {
                        if (it) saveParkingInformation(image)
                        else updateUiStateToLoadingParking(false)
                    }
                }
            }
        }
    }

    private fun stopParking() {
        viewModelScope.launch {
            _uiState.asLoaded()?.data?.parkedUiState?.parkingInformation?.imagePath?.let { picturePath ->
                deleteCameraPicture(picturePath)
            }
            clearParkingInformation()
            updatePlatformSpecificIsParkedStatus(isParked = false)
        }
    }

    private fun takePicture(callback: ((String?) -> Unit)? = null) {
        fun takePicture() {
            viewModelScope.launch {
                takeCameraPicture { imagePath ->
                    _uiState.asLoaded()?.data?.also { state ->
                        updateUiState(
                            GeneralUiState.Loaded(
                                state.copy(
                                    nonParkedUiState = state.nonParkedUiState.copy(
                                        imagePath = imagePath
                                    )
                                )
                            )
                        )
                        callback?.invoke(imagePath)
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
        _uiState.asLoaded()?.data?.also { state ->
            state.nonParkedUiState.imagePath?.let { deleteCameraPicture(it) }
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(nonParkedUiState = state.nonParkedUiState.copy(imagePath = null))
                )
            )
        }
    }

    private fun handleParkingError(failure: Failure? = null) {
        val error = when (failure) {
            is Failure.LocationNotFound, null -> Res.string.location_not_found
            else -> Res.string.parking_error
        }

        uiState.asLoaded()?.data?.let { state ->
            updateUiState(
                GeneralUiState.Loaded(
                    state.copy(
                        nonParkedUiState = state.nonParkedUiState.copy(
                            error = error,
                            parking = false
                        )
                    )
                )
            )
        }
    }

    private fun StateFlow<UiState>.asLoaded(): GeneralUiState.Loaded<MainUiState>? =
        this.value as? GeneralUiState.Loaded
}
