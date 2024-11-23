package com.jmp.wayback.presentation.main.view.nonparked

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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
) {
    ConstraintLayout(modifier = modifier) {
        val (header, body, footer) = createRefs()

        NonParkedScreenHeader(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(header) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(start = 32.dp),
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
                text = stringResource(uiState.parkButtonText),
                color = Color.Black,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
