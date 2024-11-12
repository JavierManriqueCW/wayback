package com.jmp.wayback.data.di

import com.jmp.wayback.di.provider.DependencyInjectionModulesProvider
import com.jmp.wayback.data.RepositoryImplementation
import com.jmp.wayback.domain.repository.Repository
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

object DataDependencies : DependencyInjectionModulesProvider {
    override val modules: List<Module>
        get() = listOf(
            module { factoryOf(::RepositoryImplementation) bind Repository::class }
        )
}