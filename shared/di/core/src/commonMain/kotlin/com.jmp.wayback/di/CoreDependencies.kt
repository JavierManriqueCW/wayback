package com.jmp.wayback.di

import com.jmp.wayback.data.di.DataDependencies
import com.jmp.wayback.di.provider.DependencyInjectorModules
import com.jmp.wayback.domain.di.DomainDependencies
import org.koin.core.module.Module

object CoreDependencies : DependencyInjectorModules {
    override val modules: List<Module>
        get() = listOf(
            DomainDependencies.modules,
            DataDependencies.modules
        ).flatten()
}
