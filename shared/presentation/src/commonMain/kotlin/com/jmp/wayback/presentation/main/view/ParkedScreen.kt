package com.jmp.wayback.presentation.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import wayback.shared.presentation.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ParkedScreen(
    modifier: Modifier,
    uiState: ParkedUiState,
    onStopParkingClicked: () -> Unit
) {

    Box(modifier = modifier) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(
                    horizontal = 32.dp,
                    vertical = 64.dp
                )
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = stringResource(uiState.title),
                color = Color.White,
                fontSize = 40.dp.value.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .size(48.dp),
                painter = painterResource(uiState.titleImage),
                contentDescription = stringResource(uiState.titleImageDescription)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 32.dp

                )
        ) {
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

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 32.dp,
                    bottom = 40.dp
                ),
            shape = RoundedCornerShape(45),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            onClick = onStopParkingClicked
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(uiState.stopParkingButtonText),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
