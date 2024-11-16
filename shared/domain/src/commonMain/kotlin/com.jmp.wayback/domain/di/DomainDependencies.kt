package com.jmp.wayback.domain.di

import com.jmp.wayback.di.provider.DependencyInjectionModulesProvider
import com.jmp.wayback.domain.interactor.ClearParkingInformation
import com.jmp.wayback.domain.interactor.DisableOnboarding
import com.jmp.wayback.domain.interactor.FetchUpdatedParkingInformation
import com.jmp.wayback.domain.interactor.GetParkingState
import com.jmp.wayback.domain.interactor.SaveParkingInformation
import com.jmp.wayback.domain.interactor.ShouldShowOnboarding
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

object DomainDependencies : DependencyInjectionModulesProvider {
    override val modules: List<Module>
        get() = listOf(
            module {
                factoryOf(::DisableOnboarding)
                factoryOf(::ShouldShowOnboarding)
                factoryOf(::GetParkingState)
                factoryOf(::SaveParkingInformation)
                factoryOf(::ClearParkingInformation)
                factoryOf(::FetchUpdatedParkingInformation)
            }
        )
}
