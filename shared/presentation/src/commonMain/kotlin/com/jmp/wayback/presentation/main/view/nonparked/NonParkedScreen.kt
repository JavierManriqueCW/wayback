package com.jmp.wayback.presentation.main.view.nonparked

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmp.wayback.presentation.main.viewmodel.NonParkedUiState
import org.jetbrains.compose.resources.stringResource

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

        NonParkedScreenBody(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 32.dp

                ),
            uiState = uiState,
            onTextChanged = onTextChanged
        )

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
