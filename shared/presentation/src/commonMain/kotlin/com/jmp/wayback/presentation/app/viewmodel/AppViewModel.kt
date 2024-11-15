package com.jmp.wayback.presentation.app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.domain.interactor.ShouldShowOnboarding
import com.jmp.wayback.presentation.app.common.Screens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val shouldShowOnboardingUseCase: ShouldShowOnboarding
) : ViewModel() {

    private var _uiState = MutableStateFlow(AppUiState())
    val uiState = _uiState

    init {
        viewModelScope.launch {
            val shouldShowOnboarding = shouldShowOnboardingUseCase()
            _uiState.value = _uiState.value.copy(
                loaded = true,
                shouldShowOnboarding = shouldShowOnboarding
            )
        }
    }

    fun getStartDestination(): String =
        if (_uiState.value.shouldShowOnboarding == true) {
            Screens.Onboarding.route
        } else {
            Screens.Main.route
        }
}
