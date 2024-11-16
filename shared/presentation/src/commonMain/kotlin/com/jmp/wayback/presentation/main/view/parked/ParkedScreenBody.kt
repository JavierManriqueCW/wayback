package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
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
fun ParkedScreenBody(
    modifier: Modifier,
    uiState: ParkedUiState
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth(0.7f),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(uiState.addressTitle),
                color = Color.White,
                fontSize = 14.dp.value.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.parkingInformation.address,
                color = Color.White,
                fontSize = 14.dp.value.sp,
                fontWeight = FontWeight.ExtraLight
            )

            if (uiState.parkingInformation.detail.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.parkingInformation.detail,
                    color = Color.White,
                    fontSize = 14.dp.value.sp,
                    fontWeight = FontWeight.ExtraLight
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(uiState.dateTitle),
                color = Color.White,
                fontSize = 14.dp.value.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.parkingInformation.parkingTime,
                color = Color.White,
                fontSize = 14.dp.value.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }

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
                .align(Alignment.CenterVertically)
                .fillMaxWidth(),
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress },
            ),
            contentDescription = null
        )
    }
}
