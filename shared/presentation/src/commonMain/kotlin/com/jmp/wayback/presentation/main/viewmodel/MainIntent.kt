package com.jmp.wayback.presentation.main.viewmodel

sealed class MainIntent {

    data class UpdateDetailIntent(val detail: String) : MainIntent()

    data object TakePicture : MainIntent()

    data object RemovePicture : MainIntent()

    data object ShowStopParkingDialog : MainIntent()

    data object DismissStopParkingDialog : MainIntent()

    data object DismissParkingErrorAlert : MainIntent()

    data class ParkIntent(
        val detail: String,
        val imagePath: String?
    ) : MainIntent()

    data object StopParking : MainIntent()
}
