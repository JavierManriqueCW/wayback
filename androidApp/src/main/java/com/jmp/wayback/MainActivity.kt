package com.jmp.wayback

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.jmp.wayback.presentation.app.provider.camera.CameraProvider
import com.jmp.wayback.presentation.app.provider.location.LocationProvider
import com.jmp.wayback.presentation.app.view.AppScreen
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableFullyEdgeToEdge()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        initCameraProvider()
        initLocationProvider()

        setContent {
            AppScreen()
        }
    }

    private fun enableFullyEdgeToEdge() {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.Transparent.toArgb()
            )
        )
    }

    private fun initCameraProvider() {
        val cameraProvider: CameraProvider by inject(CameraProvider::class.java)
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
