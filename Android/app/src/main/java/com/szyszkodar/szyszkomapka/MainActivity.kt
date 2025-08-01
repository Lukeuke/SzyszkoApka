package com.szyszkodar.szyszkomapka

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Scaffold
import androidx.core.view.WindowCompat
import com.szyszkodar.szyszkomapka.presentation.mapScreen.MapScreen
import com.szyszkodar.szyszkomapka.ui.theme.SzyszkoMapkaTheme
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
            SzyszkoMapkaTheme {
                Scaffold { paddingValues ->
                    MapScreen(
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}
