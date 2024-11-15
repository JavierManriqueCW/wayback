package com.jmp.wayback.presentation.app.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmp.wayback.presentation.app.common.LoadingScreen
import com.jmp.wayback.presentation.app.common.Screens
import com.jmp.wayback.presentation.app.viewmodel.AppViewModel
import com.jmp.wayback.presentation.main.view.MainScreen
import com.jmp.wayback.presentation.onboarding.view.OnboardingScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.rememberKoinInject

@Composable
@Preview
fun AppScreen() {
    val viewModel = rememberKoinInject<AppViewModel>()
    val navController = rememberNavController()
    val uiState = viewModel.uiState.collectAsState()

    MaterialTheme {
        AnimatedVisibility(
            enter = fadeIn(),
            exit = fadeOut(tween(1000)),
            visible = !uiState.value.loaded
        ) {
            LoadingScreen(
                modifier = Modifier.fillMaxSize()
            )
        }
        AnimatedVisibility(
            enter = fadeIn(tween(1000)),
            exit = fadeOut(),
            visible = uiState.value.loaded
        ) {
            uiState.value.shouldShowOnboarding?.let {
                NavHost(
                    navController = navController,
                    startDestination = viewModel.getStartDestination()
                ) {
                    composable(Screens.Onboarding.route) {
                        OnboardingScreen(
                            navController = navController,
                            viewModel = rememberKoinInject()
                        )
                    }
                    composable(Screens.Main.route) {
                        MainScreen(viewModel = rememberKoinInject())
                    }
                }
            }
        }
    }
}
