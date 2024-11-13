package com.jmp.wayback.presentation.app.view

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
import com.jmp.wayback.presentation.app.common.Screens
import com.jmp.wayback.presentation.app.viewmodel.AppViewModel
import com.jmp.wayback.presentation.main.view.MainScreen
import com.jmp.wayback.presentation.onboarding.view.OnboardingScreen
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.rememberKoinInject
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.main_background

@Composable
@Preview
fun AppScreen() {
    val viewModel = rememberKoinInject<AppViewModel>()
    val navController = rememberNavController()
    val uiState = viewModel.uiState.collectAsState()

    MaterialTheme {
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
        } ?: run {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(Res.drawable.main_background),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    }
}
