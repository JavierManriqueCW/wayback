package com.jmp.wayback.presentation.main.view.nonparked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jmp.wayback.presentation.main.viewmodel.NonParkedUiState
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import wayback.shared.presentation.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoadingParkingScreen(
    modifier: Modifier,
    uiState: NonParkedUiState
) {
    ConstraintLayout(modifier = modifier) {
        val (lottie, animatedTexts) = createRefs()

        val lottiePath = stringResource(uiState.lottiePath)
        val composition by rememberLottieComposition {
            LottieCompositionSpec.JsonString(
                Res.readBytes(lottiePath).decodeToString()
            )
        }
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = Compottie.IterateForever
        )

        Image(
            modifier = Modifier
                .width(300.dp)
                .constrainAs(lottie) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
            contentDescription = stringResource(uiState.lottieContentDescription)
        )

        LoopingAnimatedTexts(
            texts = uiState.animatedTexts,
            modifier = Modifier
                .constrainAs(animatedTexts) {
                    start.linkTo(parent.start)
                    top.linkTo(lottie.bottom, margin = 16.dp)
                    end.linkTo(parent.end)
                }
        )
    }
}
