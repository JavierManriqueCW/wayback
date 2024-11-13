package com.jmp.wayback.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel(
    uiProvider: MainUiProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(uiProvider.provide())
    val uiState: StateFlow<MainUiState> = _uiState

    fun sendIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.UpdateDetailIntent -> updateDetail(intent.detail)
            is MainIntent.ParkIntent -> park()
        }
    }

    private fun updateDetail(detail: String) {
        _uiState.value = _uiState.value.copy(detailText = detail)
    }

    private fun park() {
        _uiState.value.detailText
    }
}
