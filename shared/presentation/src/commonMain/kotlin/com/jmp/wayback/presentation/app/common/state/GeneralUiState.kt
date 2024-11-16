package com.jmp.wayback.presentation.app.common.state

sealed class GeneralUiState<T> {

    class Loading<T>: GeneralUiState<T>()

    data class Loaded<T>(val data: T): GeneralUiState<T>()
}
