package com.jmp.wayback.presentation.main.viewmodel

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class MainUiState(
    val background: DrawableResource,
    val detailPlaceholderText: StringResource,
    val detailText: String,
    val parkButtonText: StringResource,
)
