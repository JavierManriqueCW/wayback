package com.jmp.wayback.presentation.onboarding.view

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import wayback.sharedapp.presentation.generated.resources.Res
import wayback.sharedapp.presentation.generated.resources.onboarding_first_step_background

@Composable
fun FirstStepScreen(
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.onboarding_first_step_background),
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
                text = "GET STARTED",
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            page = 1,
                            animationSpec = tween(
                                durationMillis = 1000,
                                easing = LinearOutSlowInEasing
                            )
                        )
                    }
                }
            )
        }
    }
}