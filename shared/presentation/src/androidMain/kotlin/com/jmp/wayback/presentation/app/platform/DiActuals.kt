package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import org.koin.core.module.Module
import org.koin.dsl.module

actual fun getPlatformPresentationDependencies(): List<Module> =
    listOf(
        module { single { CameraProvider() } },
        module { single { LocationProvider() } }
    )
