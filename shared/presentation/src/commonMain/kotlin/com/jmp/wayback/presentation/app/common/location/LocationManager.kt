package com.jmp.wayback.presentation.app.common.location

import org.koin.core.module.Module

expect suspend fun getLocation(): Location?

expect fun getPlatformPresentationDependencies(): List<Module>

data class Location(
    val address: String,
    val latitude: Double,
    val longitude: Double
)
