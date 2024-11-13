package com.jmp.wayback.presentation.onboarding.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.valentinilk.shimmer.LocalShimmerTheme
import com.valentinilk.shimmer.defaultShimmerTheme
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@Composable
fun BouncingButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit
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
                shaderColors = listOf(
                    Color.White.copy(alpha = 1f),
                    Color.White.copy(alpha = 0.6f),
                    Color.White.copy(alpha = 1f),
                ),
                animationSpec = keyframes {
                    durationMillis = 1000
                    shimmerAnimation.value
                }
            )
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = MutableInteractionSource(),
                        indication = null,
                        role = Role.Button,
                        onClick = { onClick() }
                    )
                    .shimmer(),
                fontSize = 22.dp.value.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                text = text
            )
        }
    }
}
