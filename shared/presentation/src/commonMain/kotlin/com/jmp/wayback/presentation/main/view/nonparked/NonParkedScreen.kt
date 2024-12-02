package com.jmp.wayback.presentation.main.view.nonparked

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.jmp.wayback.presentation.app.common.compose.Colors.DialogActionColor
import com.jmp.wayback.presentation.app.common.compose.Colors.DialogContainerColor
import com.jmp.wayback.presentation.app.common.compose.fadingEdge
import com.jmp.wayback.presentation.main.viewmodel.NonParkedUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun NonParkedScreen(
    modifier: Modifier,
    uiState: NonParkedUiState,
    onTextChanged: (String) -> Unit,
    onCameraButtonClicked: () -> Unit,
    onRemovePictureClicked: () -> Unit,
    onParkClicked: () -> Unit,
    onDismissAlertRequest: () -> Unit,
) {
    ConstraintLayout(modifier = modifier) {
        val (header, body, footer, alert) = createRefs()

        NonParkedScreenHeader(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            uiState = uiState
        )

        NonParkedScreenBody(
            modifier = Modifier
                .fadingEdge(
                    topStartFadingPoint = 0.1f,
                    bottomStartFadingPoint = 0.9f
                )
                .verticalScroll(rememberScrollState())
                .constrainAs(body) {
                    top.linkTo(header.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(footer.top)
                    height = Dimension.fillToConstraints
                }
                .fillMaxWidth()
                .padding(vertical = 32.dp)
                .navigationBarsPadding(),
            uiState = uiState,
            onTextChanged = onTextChanged,
            onCameraButtonClicked = onCameraButtonClicked,
            onRemovePictureClicked = onRemovePictureClicked,
            onEnterPressed = onParkClicked
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .constrainAs(footer) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            shape = RoundedCornerShape(45),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            onClick = onParkClicked
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(uiState.parkButtonText).uppercase(),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(alert) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            visible = uiState.error != null,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            uiState.error?.let { error ->
                AlertDialog(
                    modifier = Modifier.fillMaxWidth(),
                    onDismissRequest = onDismissAlertRequest,
                    backgroundColor = DialogContainerColor,
                    shape = RoundedCornerShape(15),
                    title = {
                        Text(
                            text = stringResource(uiState.errorAlertTitle),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.LightGray
                        )
                    },
                    text = {
                        Text(
                            text = stringResource(error),
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            color = Color.LightGray
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = onDismissAlertRequest,
                            colors = ButtonDefaults.textButtonColors(
                                backgroundColor = Color.Transparent,
                                contentColor = Color.White
                            )
                        ) {
                            Text(
                                text = stringResource(uiState.errorAlertConfirmButtonText),
                                color = DialogActionColor,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                )
            }
        }
    }
}
