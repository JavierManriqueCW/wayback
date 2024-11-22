package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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
    ConstraintLayout(modifier = modifier) {
        val (mapButton, imageContainer, forwardButton) = createRefs()

        if (uiState.parkingInformation.latitude != 0.0 &&
            uiState.parkingInformation.longitude != 0.0
        ) {
            IconButton(
                modifier = Modifier
                    .constrainAs(mapButton) {
                        top.linkTo(imageContainer.top)
                        end.linkTo(imageContainer.start, 16.dp)
                        bottom.linkTo(imageContainer.bottom)
                    },
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

            Box(
                modifier = Modifier
                    .constrainAs(imageContainer) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                val imagePath = uiState.parkingInformation.imagePath
                val imageBitmap = remember(imagePath) {
                    imagePath?.toImageBitmap()
                }
                imageBitmap?.let {
                    Image(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(50))
                            .clickable { imagePath?.let { showPicture(imagePath) } }
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(50)
                            ),
                        bitmap = imageBitmap,
                        contentScale = ContentScale.Crop,
                        contentDescription = null
                    )
                }
            }

            IconButton(
                modifier = Modifier.constrainAs(forwardButton) {
                    start.linkTo(imageContainer.end, 16.dp)
                    top.linkTo(imageContainer.top)
                    bottom.linkTo(imageContainer.bottom)
                },
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