package com.jmp.wayback.presentation.app.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.loading_lottie_path
import wayback.shared.presentation.generated.resources.loading_content_description

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        val lottiePath = stringResource(Res.string.loading_lottie_path)
        val composition by rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes(lottiePath).decodeToString()
            )
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = Compottie.IterateForever
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black.copy(alpha = 0.5f))
        )

        Image(
            modifier = Modifier
                .align(Alignment.Center)
                .size(200.dp),
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
            contentDescription = stringResource(Res.string.loading_content_description),
        )
    }
}