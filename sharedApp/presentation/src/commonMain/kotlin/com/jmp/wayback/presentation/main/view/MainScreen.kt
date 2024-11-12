package com.jmp.wayback.presentation.main.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jmp.wayback.presentation.app.Greeting
import org.jetbrains.compose.resources.painterResource
import wayback.sharedapp.presentation.generated.resources.Res
import wayback.sharedapp.presentation.generated.resources.compose_multiplatform
import wayback.sharedapp.presentation.generated.resources.onboarding_second_step_background

@Composable
fun MainScreen() {
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

        var showContent by remember { mutableStateOf(false) }

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
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = { showContent = !showContent }
            ) {
                Text("Click me!")
            }
            AnimatedVisibility(showContent) {
                val greeting = remember { Greeting().greet() }
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)
                    Text("Compose: $greeting")
                }
            }
        }
    }
}
