package com.jmp.wayback.presentation.main.view.nonparked

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jmp.wayback.presentation.app.common.compose.Bouncer
import com.jmp.wayback.presentation.app.platform.showPicture
import com.jmp.wayback.presentation.app.platform.toImageBitmap
import com.jmp.wayback.presentation.main.viewmodel.NonParkedUiState
import com.valentinilk.shimmer.shimmer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun NonParkedScreenBody(
    modifier: Modifier,
    uiState: NonParkedUiState,
    onTextChanged: (String) -> Unit,
    onCameraButtonClicked: () -> Unit,
    onRemovePictureClicked: () -> Unit,
    onEnterPressed: () -> Unit
) {
    Box(modifier = modifier) {
        ConstraintLayout(
            modifier = Modifier.align(Alignment.Center)
        ) {
            val (cameraComponent, detailTextField) = createRefs()

            Box(
                modifier = Modifier
                    .constrainAs(cameraComponent) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(detailTextField.top)
                    }
            ) {
                val imageBitmap = remember(uiState.imagePath) { uiState.imagePath?.toImageBitmap() }
                val imagePath = remember(uiState.imagePath) { uiState.imagePath }

                imageBitmap?.let {
                    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                        val (retakePictureButton, image, closeButton) = createRefs()

                        IconButton(
                            modifier = Modifier
                                .size(24.dp)
                                .constrainAs(retakePictureButton) {
                                    top.linkTo(image.top)
                                    end.linkTo(image.start, margin = 16.dp)
                                    bottom.linkTo(image.bottom)
                                },
                            onClick = onCameraButtonClicked
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(uiState.refreshIcon),
                                contentDescription = stringResource(uiState.refreshIconContentDescription),
                                tint = Color.Gray
                            )
                        }

                        Image(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(50))
                                .clickable { imagePath?.let { showPicture(imagePath) } }
                                .border(
                                    width = 1.dp,
                                    color = Color.White,
                                    shape = RoundedCornerShape(50)
                                )
                                .constrainAs(image) {
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            bitmap = imageBitmap,
                            contentScale = ContentScale.Crop,
                            contentDescription = null
                        )

                        IconButton(
                            modifier = Modifier
                                .size(24.dp)
                                .constrainAs(closeButton) {
                                    start.linkTo(image.end, margin = 16.dp)
                                    top.linkTo(image.top)
                                    bottom.linkTo(image.bottom)
                                },
                            onClick = onRemovePictureClicked
                        ) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(uiState.closeIcon),
                                contentDescription = stringResource(uiState.closeIconContentDescription),
                                tint = Color.Gray
                            )
                        }
                    }
                } ?: run {
                    Bouncer(
                        shimmerColors = listOf(
                            Color.White.copy(alpha = 1f),
                            Color.White.copy(alpha = 0.2f),
                            Color.White.copy(alpha = 1f)
                        )
                    ) {
                        IconButton(onClick = onCameraButtonClicked) {
                            Image(
                                modifier = Modifier
                                    .size(150.dp)
                                    .shimmer(),
                                painter = painterResource(uiState.cameraButtonImage),
                                contentDescription = stringResource(uiState.cameraButtonContentDescription),
                                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.85f))
                            )
                        }
                    }
                }
            }

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(detailTextField) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    },
                value = uiState.detailText,
                onValueChange = onTextChanged,
                keyboardActions = KeyboardActions(
                    onDone = { onEnterPressed() }
                ),
                placeholder = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(uiState.detailPlaceholderText),
                        textAlign = TextAlign.Center,
                        color = Color.Gray.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.White,
                    cursorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color.Transparent
                ),
                maxLines = 4,
                shape = RoundedCornerShape(45),
                textStyle = TextStyle.Default.copy(
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Light,
                    fontSize = 14.sp
                )
            )
        }
    }
}
