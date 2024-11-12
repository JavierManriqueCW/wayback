package com.jmp.wayback.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmp.wayback.domain.interactor.DisableOnboarding
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val disableOnboarding: DisableOnboarding
) : ViewModel() {

    fun disableOnboarding() {
        viewModelScope.launch {
            disableOnboarding.invoke()
        }
    }
}
