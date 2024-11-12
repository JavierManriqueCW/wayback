package com.jmp.wayback.presentation.app

sealed class Screens(
    val route: String
) {

    data object Onboarding : Screens("onboarding")

    data object Main : Screens("main")
}