package com.jmp.wayback.di.provider

import org.koin.core.module.Module

interface DependencyInjectionModulesProvider {
    val modules: List<Module>
}
