package com.jmp.wayback.presentation.onboarding.viewmodel

import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.onboarding_background
import wayback.shared.presentation.generated.resources.onboarding_button_text

class OnboardingUiProvider {

    fun provide(): OnboardingUiState =
        OnboardingUiState(
            background = Res.drawable.onboarding_background,
            buttonText = Res.string.onboarding_button_text,
        )
}
