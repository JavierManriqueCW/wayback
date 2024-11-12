package com.jmp.wayback.presentation.onboarding.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import wayback.sharedapp.presentation.generated.resources.Res
import wayback.sharedapp.presentation.generated.resources.onboarding_second_step_background

@Composable
fun SecondStepScreen(
    onButtonClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.onboarding_second_step_background),
            contentDescription = null,
            contentScale = ContentScale.Crop,
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 32.dp

                ),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(32.dp),
                    color = Color.White,
                    text = "Hey you! Forget about wandering around the parking lot"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.Black.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Text(
                    modifier = Modifier.padding(32.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Light,
                    text = "With our app, locate your vehicle precisely, anytime!"
                )
            }
        }

        BouncingButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(
                    start = 32.dp,
                    top = 0.dp,
                    end = 16.dp,
                    bottom = 32.dp

                ),
            text = "GO",
            onClick = onButtonClick
        )
    }
}
