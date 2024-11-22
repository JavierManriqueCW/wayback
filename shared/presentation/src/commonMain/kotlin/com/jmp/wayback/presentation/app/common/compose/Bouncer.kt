package com.jmp.wayback.presentation.app.common.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun Bouncer(
    modifier: Modifier = Modifier,
    shimmerColors: List<Color>? = null,
    button: @Composable () -> Unit
) {
    val offsetY = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            repeat(2) {
                offsetY.animateTo(
                    targetValue = -20f,
                    animationSpec = tween(durationMillis = 300)
                )
                offsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(durationMillis = 300)
                )
            }
            delay(4000)
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .offset { IntOffset(0, offsetY.value.roundToInt()) }
            .padding(16.dp)
    ) {
        val shimmerAnimation = remember { Animatable(1f) }

        LaunchedEffect(Unit) {
            while (true) {
                repeat(3) {
                    shimmerAnimation.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    shimmerAnimation.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 500,
                            easing = LinearOutSlowInEasing
                        )
                    )
                }
                delay(1000)
            }
        }

        CompositionLocalProvider(
            LocalShimmerTheme provides defaultShimmerTheme.copy(
                blendMode = BlendMode.DstIn,
                animationSpec = keyframes {
                    durationMillis = 1000
                    shimmerAnimation.value
                }
            ).run { shimmerColors?.let { copy(shaderColors = shimmerColors) } ?: this }
        ) {
            button()
        }
    }
}
