package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState

@Composable
fun ParkedScreenBody(
    modifier: Modifier,
    uiState: ParkedUiState
) {
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            ParkedScreenBodyInformation(
                modifier = Modifier
                    .fillMaxWidth()
                   ,
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(48.dp))

            ParkedScreenBodyActions(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                uiState = uiState
            )

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}
