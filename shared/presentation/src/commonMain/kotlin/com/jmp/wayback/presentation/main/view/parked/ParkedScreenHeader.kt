package com.jmp.wayback.presentation.main.view.parked

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ParkedScreenHeader(
    modifier: Modifier,
    uiState: ParkedUiState
) {
    ConstraintLayout(modifier = modifier) {
        val (title, icon) = createRefs()

        Box(
            modifier = Modifier.constrainAs(title) {
                start.linkTo(parent.start)
                top.linkTo(icon.top)
                end.linkTo(icon.end)
                bottom.linkTo(icon.bottom)
            }
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart),
                text = stringResource(uiState.title),
                color = Color.White,
                fontSize = 40.sp,
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
            )
        }

        Box(
            modifier = Modifier.constrainAs(icon) {
                end.linkTo(parent.end)
            }
        ) {
            Image(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp),
                painter = painterResource(uiState.titleIcon),
                contentDescription = null,
                alignment = Alignment.CenterEnd
            )
        }
    }
}
