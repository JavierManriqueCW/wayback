package com.jmp.wayback.presentation.onboarding.view

import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.jmp.wayback.presentation.app.Screens
import com.jmp.wayback.presentation.onboarding.viewmodel.OnboardingViewModel
import org.koin.compose.koinInject


@Composable
fun OnboardingScreen(
    navController: NavController
) {
    val onboardingViewModel: OnboardingViewModel = koinInject()
    val pagerState = rememberPagerState(pageCount = { 2 })
    VerticalPager(state = pagerState) { page ->
        when (page) {
            0 -> {
                FirstStepScreen(
                    pagerState = pagerState
                )
            }

            1 -> {
                SecondStepScreen(
                    onButtonClick = {
                        onboardingViewModel.disableOnboarding()
                        navController.navigate(Screens.Main.route)
                    }
                )
            }
        }
    }
}
