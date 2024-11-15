package com.jmp.wayback.presentation.main.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun NonParkedScreen(
    modifier: Modifier,
    uiState: NonParkedUiState,
    onTextChanged: (String) -> Unit,
    onParkClicked: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(
                    horizontal = 32.dp,
                    vertical = 64.dp
                )
        ) {
            Text(
                text = stringResource(uiState.title),
                color = Color.White,
                fontSize = 40.dp.value.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(uiState.description),
                color = Color.White,
                fontSize = 40.dp.value.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 32.dp

                ),
            verticalArrangement = Arrangement.Center
        ) {
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
                    .align(Alignment.CenterHorizontally)
                    .width(300.dp),
                painter = rememberLottiePainter(
                    composition = composition,
                    progress = { progress },
                ),
                contentDescription = stringResource(uiState.lottieContentDescription)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = uiState.detailText,
                onValueChange = onTextChanged,
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(uiState.detailPlaceholderText),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                maxLines = 2,
                shape = RoundedCornerShape(45),
                textStyle = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light
                )
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
            onClick = onParkClicked
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(uiState.parkButtonText),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
