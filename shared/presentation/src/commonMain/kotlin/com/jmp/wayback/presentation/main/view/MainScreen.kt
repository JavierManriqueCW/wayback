package com.jmp.wayback.presentation.main.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.jmp.wayback.presentation.app.common.compose.LoadingScreen
import com.jmp.wayback.presentation.app.common.compose.clearFocusOnTap
import com.jmp.wayback.presentation.app.common.state.GeneralUiState
import com.jmp.wayback.presentation.main.view.nonparked.LoadingParkingScreen
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
                val isParking = generalUiState.data.nonParkedUiState.parking

                AnimatedVisibility(
                    enter = fadeIn(tween(1000)),
                    exit = fadeOut(),
                    visible = isParking
                ) {
                    LoadingParkingScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        uiState = generalUiState.data.nonParkedUiState
                    )
                }

                AnimatedVisibility(
                    enter = fadeIn(tween(1000)),
                    exit = fadeOut(),
                    visible = !isParking
                ) {
                    val isParked = generalUiState.data.parkedUiState != null
                    if (isParked) {
                        ParkedScreen(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 32.dp),
                            uiState = generalUiState.data.parkedUiState as ParkedUiState,
                            stopParking = { viewModel.sendIntent(MainIntent.StopParking) },
                            onStopParkingClicked = { viewModel.sendIntent(MainIntent.ShowStopParkingDialog) },
                            onDismissDialogRequest = { viewModel.sendIntent(MainIntent.DismissStopParkingDialog) }
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
                                .systemBarsPadding()
                                .clearFocusOnTap()
                                .padding(
                                    start = 32.dp,
                                    top = 64.dp,
                                    end = 32.dp,
                                    bottom = 32.dp
                                ),
                            uiState = generalUiState.data.nonParkedUiState,
                            onTextChanged = {
                                if (it.length <= generalUiState.data.nonParkedUiState.maxCharacters) {
                                    viewModel.sendIntent(MainIntent.UpdateDetailIntent(it))
                                }
                            },
                            onCameraButtonClicked = {
                                viewModel.sendIntent(MainIntent.TakePicture)
                            },
                            onRemovePictureClicked = {
                                viewModel.sendIntent(MainIntent.RemovePicture)
                            },
                            onParkClicked = {
                                viewModel.sendIntent(
                                    MainIntent.ParkIntent(
                                        detail = generalUiState.data.nonParkedUiState.detailText.trimStart().trimEnd(),
                                        imagePath = generalUiState.data.nonParkedUiState.imagePath
                                    )
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
