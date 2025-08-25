package com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.szyszkodar.szyszkomapka.data.intentsHandling.GoogleMapsOpener
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.presentation.bookpointInfoBottomSheet.components.SheetActionButton
import org.maplibre.android.geometry.LatLng

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookpointBottomSheet(
    bookpoint: BookpointUI,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = bottomSheetState,

        modifier = modifier
            .padding(horizontal = 10.dp)
            .padding(top = 30.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = bookpoint.title,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )
            Spacer(Modifier.height(20.dp))

            Text(
                text = bookpoint.description,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(40.dp))

            SheetActionButton(
                name = "Pokaż trasę",
                buttonIcon = Icons.Default.LocationOn,
                onClick = {
                    val mapsOpener = GoogleMapsOpener(context)

                    val result = mapsOpener.openMapsRoute(LatLng(bookpoint.latitude, bookpoint.longitude))
                    if (result != null) Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                }
            )
            if (!bookpoint.approved) {
                Spacer(Modifier.height(10.dp))

                SheetActionButton(
                    name = "Zaakceptuj",
                    buttonIcon = Icons.Default.Check,
                    onClick = {
                        // TODO: Zaakceptuj biblioteczkę
                    }
                )
            }

            Spacer(Modifier.height(60.dp))
        }
    }
}