package com.jmp.wayback.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.domain.interactor.DisableOnboarding
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val disableOnboarding: DisableOnboarding,
    uiProvider: OnboardingUiProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow(uiProvider.provide())
    val uiState: StateFlow<OnboardingUiState> = _uiState

    fun disableOnboarding() {
        viewModelScope.launch {
            disableOnboarding.invoke()
        }
    }
}
