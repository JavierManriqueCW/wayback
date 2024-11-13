package com.jmp.wayback.presentation

import com.jmp.wayback.presentation.app.common.di.KoinDependencies
import org.koin.core.context.startKoin

fun initKoin() {
    startKoin {
        modules(KoinDependencies.modules)
    }
}
