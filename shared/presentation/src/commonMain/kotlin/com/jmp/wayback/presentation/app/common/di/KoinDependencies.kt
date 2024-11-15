package com.jmp.wayback.presentation.app.common.di

import com.jmp.wayback.di.CoreDependencies
import com.jmp.wayback.domain.interactor.ClearParkingInformation
import com.jmp.wayback.domain.interactor.DisableOnboarding
import com.jmp.wayback.domain.interactor.FetchUpdatedParkingInformation
import com.jmp.wayback.domain.interactor.GetParkingState
import com.jmp.wayback.domain.interactor.SaveParkingInformation
import com.jmp.wayback.domain.interactor.ShouldShowOnboarding
import com.jmp.wayback.presentation.app.common.location.getPlatformPresentationDependencies
import com.jmp.wayback.presentation.app.viewmodel.AppViewModel
import com.jmp.wayback.presentation.main.viewmodel.MainUiProvider
import com.jmp.wayback.presentation.main.viewmodel.MainViewModel
import com.jmp.wayback.presentation.onboarding.viewmodel.OnboardingUiProvider
import com.jmp.wayback.presentation.onboarding.viewmodel.OnboardingViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

object KoinDependencies {
    val modules: List<Module>
        get() = listOf(
            CoreDependencies.modules,
            getPlatformPresentationDependencies(),
            listOf(
                getAppViewModelModule(),
                getOnboardingViewModelModule(),
                getMainViewModelModule()
            )
        ).flatten()

    private fun getAppViewModelModule() =
        module { factory { AppViewModel(get<ShouldShowOnboarding>()) } }

    private fun getOnboardingViewModelModule() =
        module {
            factory { OnboardingUiProvider() }
            factory {
                OnboardingViewModel(
                    get<DisableOnboarding>(),
                    get<OnboardingUiProvider>()
                )
            }
        }

    private fun getMainViewModelModule() =
        module {
            factory { MainUiProvider() }
            factory {
                MainViewModel(
                    get<FetchUpdatedParkingInformation>(),
                    get<GetParkingState>(),
                    get<SaveParkingInformation>(),
                    get<ClearParkingInformation>(),
                    get<MainUiProvider>()
                )
            }
        }
}
