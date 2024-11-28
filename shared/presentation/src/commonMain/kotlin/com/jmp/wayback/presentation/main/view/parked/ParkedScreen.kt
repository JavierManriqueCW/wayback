package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
import org.jetbrains.compose.resources.stringResource

@Composable
fun ParkedScreen(
    modifier: Modifier,
    uiState: ParkedUiState,
    onDismissDialogRequest: () -> Unit,
    onStopParkingClicked: () -> Unit,
    stopParking: () -> Unit
) {
    ConstraintLayout(modifier = modifier) {
        val (
            headerContainer,
            bodyContainer,
            buttonContainer,
            dialog
        ) = createRefs()

        ParkedScreenHeader(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .constrainAs(headerContainer) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                }
                .padding(top = 64.dp),
            uiState = uiState
        )

        ParkedScreenBody(
            modifier = Modifier
                .fadingEdge(
                    topStartFadingPoint = 0.1f,
                    bottomStartFadingPoint = 0.9f
                )
                .verticalScroll(rememberScrollState())
                .constrainAs(bodyContainer) {
                    start.linkTo(parent.start)
                    top.linkTo(headerContainer.bottom)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttonContainer.top)
                    height = Dimension.fillToConstraints
                }
                .padding(horizontal = 32.dp),
            uiState = uiState
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 32.dp,
                    bottom = 32.dp
                )
                .constrainAs(buttonContainer) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            shape = RoundedCornerShape(45),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White
            ),
            onClick = onStopParkingClicked
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = stringResource(uiState.stopParkingButtonText).uppercase(),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }

        AnimatedVisibility(
            modifier = Modifier.constrainAs(dialog) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            },
            visible = uiState.stopParkingDialogState.visible,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            AlertDialog(
                modifier = Modifier.fillMaxWidth(),
                onDismissRequest = onDismissDialogRequest,
                backgroundColor = DialogContainerColor,
                shape = RoundedCornerShape(15),
                title = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(uiState.stopParkingDialogState.title),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Text(
                        text = stringResource(uiState.stopParkingDialogState.body),
                        fontWeight = FontWeight.Light,
                        fontSize = 15.sp,
                        color = Color.LightGray
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = stopParking,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(uiState.stopParkingDialogState.positiveAction),
                            color = DialogActionColor,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = onDismissDialogRequest,
                        colors = ButtonDefaults.textButtonColors(
                            backgroundColor = Color.Transparent,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = stringResource(uiState.stopParkingDialogState.negativeAction),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            )
        }
    }
}
