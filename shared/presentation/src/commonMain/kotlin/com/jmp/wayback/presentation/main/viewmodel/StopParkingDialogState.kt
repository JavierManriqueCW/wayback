package com.jmp.wayback.presentation.main.viewmodel

import org.jetbrains.compose.resources.StringResource

data class StopParkingDialogState(
    val visible: Boolean = false,
    val title: StringResource,
    val body: StringResource,
    val positiveAction: StringResource,
    val negativeAction: StringResource
)
