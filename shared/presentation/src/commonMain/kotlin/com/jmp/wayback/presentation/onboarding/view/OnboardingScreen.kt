package com.jmp.wayback.presentation.onboarding.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.jmp.wayback.presentation.app.common.Screens
import com.jmp.wayback.presentation.onboarding.viewmodel.OnboardingViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: OnboardingViewModel
) {
    val uiState = viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(uiState.value.background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Row(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 32.dp

                ),
            horizontalArrangement = Arrangement.Center
        ) {
            BouncingButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Bottom),
                text = stringResource(uiState.value.buttonText),
                onClick = {
                    scope.launch {
                        viewModel.disableOnboarding()
                        navController.navigate(Screens.Main.route) {
                            popUpTo(Screens.Onboarding.route) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}
