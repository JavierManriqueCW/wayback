package com.jmp.wayback.presentation.main.viewmodel

import com.jmp.wayback.common.ParkingInformation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class ParkedUiState(
    val stopParkingDialogState: StopParkingDialogState,
    val title: StringResource,
    val titleIcon: DrawableResource,
    val titleImageDescription: StringResource,
    val mapIcon: DrawableResource,
    val mapIconContentDescription: StringResource,
    val eyeIcon: DrawableResource,
    val eyeIconContentDescription: StringResource,
    val forwardIcon: DrawableResource,
    val forwardIconContentDescription: StringResource,
    val addressTitle: StringResource,
    val dateTitle: StringResource,
    val detailTitle: StringResource,
    val lottiePath: StringResource,
    val stopParkingButtonText: StringResource,
    val parkingInformation: ParkingInformation
)
