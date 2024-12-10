package com.jmp.wayback

import androidx.activity.compose.BackHandler
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.jmp.wayback.presentation.app.provider.camera.CameraProvider

@Composable
fun CameraScreen(cameraProvider: CameraProvider) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }

    BackHandler(enabled = true) {
        cameraProvider.releaseCamera()
    }

    LaunchedEffect(Unit) {
        cameraProvider.startCamera(
            context = context,
            lifecycleOwner = lifecycleOwner,
            previewView = previewView,
            imageCapture = cameraProvider.imageCapture
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView }
        )

        Image(
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
                .size(32.dp)
                .align(Alignment.TopStart)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    cameraProvider.releaseCamera()
                },
            painter = painterResource(R.drawable.ic_close),
            contentDescription = "Close",
            colorFilter = ColorFilter.tint(Color.LightGray),
            contentScale = ContentScale.Fit
        )

        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 20.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    cameraProvider.captureCameraPicture(
                        context = context,
                        imageCapture = cameraProvider.imageCapture,
                        onImageCaptured = {
                            cameraProvider.apply {
                                updateCameraFlow(it.toString())
                                releaseCamera()
                            }
                        },
                        onError = {
                            cameraProvider.apply {
                                updateCameraFlow(null)
                                releaseCamera()
                            }
                        }
                    )
                },
            painter = painterResource(R.drawable.ic_lense),
            contentDescription = "Take picture",
            colorFilter = ColorFilter.tint(Color.White),
            contentScale = ContentScale.Fit
        )
    }
}
