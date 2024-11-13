package com.jmp.wayback.data.di

import com.jmp.wayback.data.getDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getPlatformModules(): List<Module> =
    listOf(
        module {
            single { getDataStore(androidContext()) }
        }
    )
