package com.jmp.wayback.presentation.main.viewmodel

import com.jmp.wayback.common.ParkingInformation
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class ParkedUiState(
    val title: StringResource,
    val titleImage: DrawableResource,
    val titleImageDescription: StringResource,
    val addressTitle: StringResource,
    val dateTitle: StringResource,
    val lottiePath: StringResource,
    val stopParkingButtonText: StringResource,
    val parkingInformation: ParkingInformation
)
