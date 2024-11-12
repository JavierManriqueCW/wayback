package com.jmp.wayback

import android.app.Application
import com.jmp.wayback.presentation.app.di.KoinDependencies
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class WaybackApplication : Application() {
    override fun onCreate() {
        startKoin {
            androidContext(this@WaybackApplication)
            modules(KoinDependencies.modules)
        }

        super.onCreate()
    }
}
