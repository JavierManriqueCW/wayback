package com.jmp.wayback.presentation.app.platform

import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

actual fun getPlatformPresentationDependencies(): List<Module> =
    listOf(
        module { singleOf(::LocationProvider) },
        module { singleOf(::CameraProvider) }
    )
