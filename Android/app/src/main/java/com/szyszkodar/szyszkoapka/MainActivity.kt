package com.szyszkodar.szyszkoapka

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import com.szyszkodar.szyszkoapka.presentation.mainScreen.MainScreen
import com.szyszkodar.szyszkoapka.presentation.mapScreen.MapScreen
import dagger.hilt.android.AndroidEntryPoint
import org.maplibre.android.MapLibre

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)


        MapLibre.getInstance(this)

        setContent {
            MaterialTheme {
                Scaffold{ paddingValues ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xff6B8F71))
                    ) {
                        MainScreen(paddingValues) {
                            MapScreen()
                        }
                    }
                }

            }
        }
    }
}
