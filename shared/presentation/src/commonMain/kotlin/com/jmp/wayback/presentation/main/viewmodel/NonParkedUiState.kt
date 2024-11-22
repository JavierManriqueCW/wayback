package com.jmp.wayback.presentation.main.viewmodel

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class NonParkedUiState(
    val title: StringResource,
    val description: StringResource,
    val lottiePath: StringResource,
    val lottieContentDescription: StringResource,
    val detailPlaceholderText: StringResource,
    val detailText: String,
    val maxCharacters: Int,
    val titleIcon: DrawableResource,
    val titleIconContentDescription: StringResource,
    val cameraButtonImage: DrawableResource,
    val cameraButtonContentDescription: StringResource,
    val refreshIcon: DrawableResource,
    val refreshIconContentDescription: StringResource,
    val closeIcon: DrawableResource = refreshIcon,
    val closeIconContentDescription: StringResource,
    val parkButtonText: StringResource,
    val imagePath: String? = null,
    val parking: Boolean = false
)
