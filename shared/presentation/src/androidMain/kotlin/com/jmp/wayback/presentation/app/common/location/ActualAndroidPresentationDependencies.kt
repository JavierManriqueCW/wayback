package com.jmp.wayback.presentation.app.common.location

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getPlatformPresentationDependencies(): List<Module> =
    listOf(module { single { LocationProvider() } })