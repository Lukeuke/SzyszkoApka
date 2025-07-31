package com.szyszkodar.szyszkomapka

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.presentation.MainViewModel
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
                val viewModel: MainViewModel = viewModel()
                val state = viewModel.state.collectAsStateWithLifecycle()

                Scaffold(
                    floatingActionButton = {
                        FloatingActionButton(
                            shape = CircleShape,
                            containerColor = MaterialTheme.colorScheme.secondary,
                            elevation = FloatingActionButtonDefaults.loweredElevation(),
                            onClick = { viewModel.toggleAppMode(AppMode.ADD_BOOKPOINT) }
                        ) { Icon(Icons.Default.Add, null) }
                    }
                ) { paddingValues ->
                    MapScreen(
                        appState = state.value,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }
}
