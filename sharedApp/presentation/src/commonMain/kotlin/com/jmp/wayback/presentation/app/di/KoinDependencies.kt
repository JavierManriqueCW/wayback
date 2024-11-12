package com.jmp.wayback.presentation.app.di

import com.jmp.wayback.di.CoreDependencies
import com.jmp.wayback.domain.interactor.DisableOnboarding
import com.jmp.wayback.domain.interactor.ShouldShowOnboarding
import com.jmp.wayback.presentation.main.viewmodel.MainViewModel
import com.jmp.wayback.presentation.onboarding.viewmodel.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinDependencies {
    val modules: List<Module>
        get() = listOf(
            CoreDependencies.modules,
            listOf(
                getOnboardingViewModelModule(),
                getMainViewModelModule()
            )
        ).flatten()

    private fun getOnboardingViewModelModule() =
        module { factory { OnboardingViewModel(get<DisableOnboarding>()) } }

    private fun getMainViewModelModule() =
        module { factory { MainViewModel(get<ShouldShowOnboarding>()) } }
}
