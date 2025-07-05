package com.szyszkodar.szyszkoapka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.szyszkodar.szyszkoapka.presentation.mapScreen.MapScreen
import dagger.hilt.android.AndroidEntryPoint
import org.maplibre.android.MapLibre

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapLibre.getInstance(this)

        setContent {
            MaterialTheme {
                val context = LocalContext.current

                MapScreen()
            }
        }
    }
}
