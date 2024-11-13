package com.jmp.wayback.presentation.main.viewmodel

sealed class MainIntent {

    data class UpdateDetailIntent(val detail: String) : MainIntent()

    data object ParkIntent : MainIntent()
}
