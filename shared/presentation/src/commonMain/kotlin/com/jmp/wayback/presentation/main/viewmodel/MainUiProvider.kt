package com.jmp.wayback.presentation.main.viewmodel

import com.jmp.wayback.common.ParkingInformation
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.asleep_emoji
import wayback.shared.presentation.generated.resources.main_background
import wayback.shared.presentation.generated.resources.non_parked_screen_description
import wayback.shared.presentation.generated.resources.non_parked_screen_detail_placeholder
import wayback.shared.presentation.generated.resources.non_parked_screen_lottie_content_description
import wayback.shared.presentation.generated.resources.non_parked_screen_lottie_path
import wayback.shared.presentation.generated.resources.non_parked_screen_park_button
import wayback.shared.presentation.generated.resources.non_parked_screen_title
import wayback.shared.presentation.generated.resources.parked_screen_address_title
import wayback.shared.presentation.generated.resources.parked_screen_date_title
import wayback.shared.presentation.generated.resources.parked_screen_lottie_path
import wayback.shared.presentation.generated.resources.parked_screen_stop_parking_button
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
            description = Res.string.non_parked_screen_description,
            lottiePath = Res.string.non_parked_screen_lottie_path,
            lottieContentDescription = Res.string.non_parked_screen_lottie_content_description,
            detailPlaceholderText = Res.string.non_parked_screen_detail_placeholder,
            detailText = String(),
            maxCharacters = MAX_DETAIL_CHARACTERS,
            parkButtonText = Res.string.non_parked_screen_park_button
        )

    private fun provideParkedUiState(parkingInformation: ParkingInformation): ParkedUiState =
        ParkedUiState(
            title = Res.string.parked_screen_title,
            titleImage = Res.drawable.asleep_emoji,
            titleImageDescription = Res.string.parked_screen_title_image_description,
            addressTitle = Res.string.parked_screen_address_title,
            dateTitle = Res.string.parked_screen_date_title,
            lottiePath = Res.string.parked_screen_lottie_path,
            stopParkingButtonText = Res.string.parked_screen_stop_parking_button,
            parkingInformation = parkingInformation
        )

    companion object {
        private const val MAX_DETAIL_CHARACTERS = 40
    }
}
