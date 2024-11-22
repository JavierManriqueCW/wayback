package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jmp.wayback.presentation.app.common.compose.Bouncer
import com.jmp.wayback.presentation.app.platform.openMap
import com.jmp.wayback.presentation.app.platform.shareParkingInformation
import com.jmp.wayback.presentation.app.platform.showPicture
import com.jmp.wayback.presentation.app.platform.toImageBitmap
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ParkedScreenBodyActions(
    modifier: Modifier,
    uiState: ParkedUiState
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        if (uiState.parkingInformation.latitude != 0.0 &&
            uiState.parkingInformation.longitude != 0.0
        ) {
            Box(modifier = Modifier.weight(1f)) {
                IconButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = {
                        openMap(
                            latitude = uiState.parkingInformation.latitude,
                            longitude = uiState.parkingInformation.longitude
                        )
                    }
                ) {
                    Icon(
                        modifier = Modifier.size(30.dp),
                        painter = painterResource(uiState.mapIcon),
                        contentDescription = stringResource(uiState.mapIconContentDescription),
                        tint = Color.White
                    )
                }
            }

            val imageBitmap = remember(uiState.parkingInformation.imagePath) {
                uiState.parkingInformation.imagePath?.toImageBitmap()
            }
            imageBitmap?.let {
                Box(modifier = Modifier.weight(1f)) {
                    Bouncer(
                        modifier = Modifier.align(Alignment.Center),
                        shimmerColors = listOf(
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 1f),
                            Color.White.copy(alpha = 0.2f)
                        )
                    ) {
                        IconButton(
                            onClick = {
                                uiState.parkingInformation.imagePath?.let { imagePath ->
                                    showPicture(imagePath)
                                }
                            }
                        ) {
                            Icon(
                                modifier = Modifier.size(40.dp),
                                painter = painterResource(uiState.eyeIcon),
                                contentDescription = stringResource(uiState.eyeIconContentDescription),
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                IconButton(
                    modifier = Modifier.align(Alignment.Center),
                    onClick = { shareParkingInformation(uiState.parkingInformation) }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(uiState.forwardIcon),
                        contentDescription = stringResource(uiState.forwardIconContentDescription),
                        tint = Color.White
                    )
                }
            }
        }
    }
}
