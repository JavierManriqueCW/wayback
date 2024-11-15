package com.jmp.wayback.presentation.main.viewmodel

import org.jetbrains.compose.resources.StringResource

data class NonParkedUiState(
    val title: StringResource,
    val description: StringResource,
    val lottiePath: StringResource,
    val lottieContentDescription: StringResource,
    val detailPlaceholderText: StringResource,
    val detailText: String,
    val maxCharacters: Int,
    val parkButtonText: StringResource
)
