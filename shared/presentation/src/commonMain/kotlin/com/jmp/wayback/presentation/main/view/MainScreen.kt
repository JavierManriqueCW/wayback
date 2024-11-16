package com.jmp.wayback.presentation.main.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.jmp.wayback.presentation.app.common.compose.LoadingScreen
import com.jmp.wayback.presentation.app.common.compose.clearFocusOnTap
import com.jmp.wayback.presentation.app.common.state.GeneralUiState
import com.jmp.wayback.presentation.main.view.nonparked.NonParkedScreen
import com.jmp.wayback.presentation.main.view.parked.ParkedScreen
import com.jmp.wayback.presentation.main.viewmodel.MainIntent
import com.jmp.wayback.presentation.main.viewmodel.MainViewModel
import com.jmp.wayback.presentation.main.viewmodel.ParkedUiState
import org.jetbrains.compose.resources.painterResource
import wayback.shared.presentation.generated.resources.Res
import wayback.shared.presentation.generated.resources.main_background

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val uiState = viewModel.uiState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            modifier = Modifier.matchParentSize(),
            painter = painterResource(Res.drawable.main_background),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
        )

        when (val generalUiState = uiState.value) {
            is GeneralUiState.Loading -> {
                LoadingScreen(
                    modifier = Modifier.fillMaxSize()
                )
            }
            is GeneralUiState.Loaded -> {
                val isParked = generalUiState.data.parkedUiState != null

                if (isParked) {
                    ParkedScreen(
                        modifier = Modifier.fillMaxSize(),
                        uiState = generalUiState.data.parkedUiState as ParkedUiState,
                        onStopParkingClicked = { viewModel.sendIntent(MainIntent.StopParking) }
                    )
                }

                AnimatedVisibility(
                    enter = EnterTransition.None,
                    exit = fadeOut(),
                    visible = !isParked
                ) {
                    NonParkedScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .clearFocusOnTap(),
                        uiState = generalUiState.data.nonParkedUiState,
                        onTextChanged = {
                            if (it.length <= generalUiState.data.nonParkedUiState.maxCharacters) {
                                viewModel.sendIntent(MainIntent.UpdateDetailIntent(it))
                            }
                        },
                        onParkClicked = {
                            viewModel.sendIntent(
                                MainIntent.ParkIntent(
                                    detail = generalUiState.data.nonParkedUiState.detailText
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}
