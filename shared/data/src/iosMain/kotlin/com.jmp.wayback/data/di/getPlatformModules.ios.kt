package com.jmp.wayback.data.di

import com.jmp.wayback.data.getDataStore
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun getPlatformModules(): List<Module> =
    listOf(
        module { singleOf(::getDataStore) }
    )
