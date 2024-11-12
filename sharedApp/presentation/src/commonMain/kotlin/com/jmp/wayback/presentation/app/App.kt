package com.jmp.wayback.presentation.app

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmp.wayback.presentation.main.view.MainScreen
import com.jmp.wayback.presentation.onboarding.view.OnboardingScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val navController = rememberNavController()

    MaterialTheme {
        NavHost(
            navController = navController,
            startDestination = Screens.Onboarding.route
        ) {
            composable(Screens.Onboarding.route) { OnboardingScreen(navController) }
            composable(Screens.Main.route) { MainScreen() }
        }
    }
}
