package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ParkedScreenBodyInformation(
    modifier: Modifier,
    uiState: ParkedUiState
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            text = stringResource(uiState.addressTitle),
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiState.parkingInformation.address,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(uiState.dateTitle),
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = uiState.parkingInformation.parkingTime,
            color = Color.White,
            fontSize = 15.sp,
            fontWeight = FontWeight.ExtraLight
        )

        if (uiState.parkingInformation.detail.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(uiState.detailTitle),
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = uiState.parkingInformation.detail,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraLight
            )
        }
    }
}
