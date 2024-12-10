package com.jmp.wayback

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import com.jmp.wayback.presentation.app.view.AppScreen
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    private val cameraProvider: CameraProvider by inject(CameraProvider::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableFullyEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        configureNavigationBar(window)

        initCameraProvider()
        initLocationProvider()

        setContent {
            val hasActiveCamera = cameraProvider.isActive.collectAsState()

            AppScreen()

            AnimatedVisibility(
                visible = hasActiveCamera.value,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CameraScreen(cameraProvider)
            }
        }
    }

    private fun enableFullyEdgeToEdge() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        )
    }

    @Suppress("DEPRECATION")
    private fun configureNavigationBar(window: Window) {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                )
    }

    private fun initCameraProvider() {
        cameraProvider.init(
            context = this,
            activity = this
        )
    }


    private fun initLocationProvider(){
        val locationProvider: LocationProvider by inject(LocationProvider::class.java)
        locationProvider.init(
            context = this,
            activity = this
        )
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppScreen()
}
