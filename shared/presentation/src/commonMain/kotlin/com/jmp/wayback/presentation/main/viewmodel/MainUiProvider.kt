package com.jmp.wayback.presentation.main.viewmodel

import com.jmp.wayback.common.ParkingInformation
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.ic_asleep
import wayback.shared.presentation.generated.resources.ic_camera
import wayback.shared.presentation.generated.resources.ic_close
import wayback.shared.presentation.generated.resources.ic_eye
import wayback.shared.presentation.generated.resources.ic_forward
import wayback.shared.presentation.generated.resources.ic_map
import wayback.shared.presentation.generated.resources.ic_refresh
import wayback.shared.presentation.generated.resources.ic_sparkles
import wayback.shared.presentation.generated.resources.main_background
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_1
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_2
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_3
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_4
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_5
import wayback.shared.presentation.generated.resources.non_parked_screen_animated_text_6
import wayback.shared.presentation.generated.resources.non_parked_screen_camera_button_content_description
import wayback.shared.presentation.generated.resources.non_parked_screen_close_button_content_description
import wayback.shared.presentation.generated.resources.non_parked_screen_detail_placeholder
import wayback.shared.presentation.generated.resources.non_parked_screen_lottie_content_description
import wayback.shared.presentation.generated.resources.non_parked_screen_lottie_path
import wayback.shared.presentation.generated.resources.non_parked_screen_park_button
import wayback.shared.presentation.generated.resources.non_parked_screen_refresh_icon_content_description
import wayback.shared.presentation.generated.resources.non_parked_screen_title
import wayback.shared.presentation.generated.resources.non_parked_screen_title_icon_content_description
import wayback.shared.presentation.generated.resources.parked_screen_address_title
import wayback.shared.presentation.generated.resources.parked_screen_date_title
import wayback.shared.presentation.generated.resources.parked_screen_detail_title
import wayback.shared.presentation.generated.resources.parked_screen_eye_icon_content_description
import wayback.shared.presentation.generated.resources.parked_screen_forward_icon_content_description
import wayback.shared.presentation.generated.resources.parked_screen_lottie_path
import wayback.shared.presentation.generated.resources.parked_screen_map_icon_content_description
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_button
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_dialog_body
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_dialog_negative_action
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_dialog_positive_action
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_dialog_title
import wayback.shared.presentation.generated.resources.parked_screen_title
import wayback.shared.presentation.generated.resources.parked_screen_title_image_description

class MainUiProvider {

    fun provide(parkingInformation: ParkingInformation?): MainUiState =
        MainUiState(
            background = Res.drawable.main_background,
            nonParkedUiState = provideNonParkedUiState(),
            parkedUiState = parkingInformation?.let { provideParkedUiState(it) }
        )
    
    private fun provideNonParkedUiState(): NonParkedUiState =
        NonParkedUiState(
            title = Res.string.non_parked_screen_title,
            lottiePath = Res.string.non_parked_screen_lottie_path,
            lottieContentDescription = Res.string.non_parked_screen_lottie_content_description,
            detailPlaceholderText = Res.string.non_parked_screen_detail_placeholder,
            detailText = String(),
            maxCharacters = MAX_DETAIL_CHARACTERS,
            titleIcon = Res.drawable.ic_sparkles,
            titleIconContentDescription = Res.string.non_parked_screen_title_icon_content_description,
            cameraButtonImage = Res.drawable.ic_camera,
            cameraButtonContentDescription = Res.string.non_parked_screen_camera_button_content_description,
            refreshIcon = Res.drawable.ic_refresh,
            refreshIconContentDescription = Res.string.non_parked_screen_refresh_icon_content_description,
            closeIcon = Res.drawable.ic_close,
            closeIconContentDescription = Res.string.non_parked_screen_close_button_content_description,
            parkButtonText = Res.string.non_parked_screen_park_button,
            animatedTexts = listOf(
                Res.string.non_parked_screen_animated_text_1,
                Res.string.non_parked_screen_animated_text_2,
                Res.string.non_parked_screen_animated_text_3,
                Res.string.non_parked_screen_animated_text_4,
                Res.string.non_parked_screen_animated_text_5,
                Res.string.non_parked_screen_animated_text_6
            )
        )

    private fun provideParkedUiState(parkingInformation: ParkingInformation): ParkedUiState =
        ParkedUiState(
            stopParkingDialogState = provideStopParkingDialogState(),
            title = Res.string.parked_screen_title,
            titleIcon = Res.drawable.ic_asleep,
            titleImageDescription = Res.string.parked_screen_title_image_description,
            mapIcon = Res.drawable.ic_map,
            mapIconContentDescription = Res.string.parked_screen_map_icon_content_description,
            eyeIcon = Res.drawable.ic_eye,
            eyeIconContentDescription = Res.string.parked_screen_eye_icon_content_description,
            forwardIcon = Res.drawable.ic_forward,
            forwardIconContentDescription = Res.string.parked_screen_forward_icon_content_description,
            addressTitle = Res.string.parked_screen_address_title,
            dateTitle = Res.string.parked_screen_date_title,
            detailTitle = Res.string.parked_screen_detail_title,
            lottiePath = Res.string.parked_screen_lottie_path,
            stopParkingButtonText = Res.string.parked_screen_stop_parking_button,
            parkingInformation = parkingInformation
        )

    private fun provideStopParkingDialogState(): StopParkingDialogState =
        StopParkingDialogState(
            title = Res.string.parked_screen_stop_parking_dialog_title,
            body = Res.string.parked_screen_stop_parking_dialog_body,
            positiveAction = Res.string.parked_screen_stop_parking_dialog_positive_action,
            negativeAction = Res.string.parked_screen_stop_parking_dialog_negative_action
        )

    companion object {
        private const val MAX_DETAIL_CHARACTERS = 45
    }
}
