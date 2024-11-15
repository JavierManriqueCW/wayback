package com.jmp.wayback.presentation.main.viewmodel

import org.jetbrains.compose.resources.DrawableResource

data class MainUiState(
    val background: DrawableResource,
    val nonParkedUiState: NonParkedUiState,
    val parkedUiState: ParkedUiState?
)
