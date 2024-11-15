package com.jmp.wayback

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.jmp.wayback.presentation.app.common.location.LocationProvider
import com.jmp.wayback.presentation.app.view.AppScreen
import org.koin.java.KoinJavaComponent.inject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableFullyEdgeToEdge()
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val locationProvider: LocationProvider by inject(LocationProvider::class.java)
        locationProvider.init(this, this)

        setContent {
            AppScreen()
        }
    }

    private fun enableFullyEdgeToEdge() {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                Color.Transparent.toArgb(),
                Color.Transparent.toArgb()
            )
        )
        if (Build.VERSION.SDK_INT >= 29) {
            window.isNavigationBarContrastEnforced = false
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    AppScreen()
}