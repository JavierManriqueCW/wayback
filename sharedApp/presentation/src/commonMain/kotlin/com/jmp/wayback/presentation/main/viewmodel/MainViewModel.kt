package com.jmp.wayback.presentation.main.viewmodel

import androidx.lifecycle.ViewModel
import com.jmp.wayback.domain.interactor.ShouldShowOnboarding

class MainViewModel(
    private val shouldShowOnboarding: ShouldShowOnboarding
) : ViewModel() {

}
