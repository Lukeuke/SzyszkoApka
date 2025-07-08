package com.szyszkodar.szyszkoapka.presentation.mapScreen.components

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.szyszkodar.szyszkoapka.R
import dagger.hilt.android.qualifiers.ApplicationContext
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapView
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconIgnorePlacement
import org.maplibre.android.style.layers.PropertyFactory.iconImage
import org.maplibre.android.style.layers.PropertyFactory.iconSize
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

@Composable
fun MapLibreView (
    context: Context,
    modifier: Modifier = Modifier
) {
    AndroidView(modifier = modifier, factory = {
        val mapView = MapView(context)

        mapView.getMapAsync { map ->
            map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
                // 1. Dodaj obraz jako ikonę markera
                style.addImage(
                    "marker-icon",
                    BitmapFactory.decodeResource(context.resources, R.drawable.marker)
                )

                // 2. Zbuduj GeoJSON punkt
                val geoJson = FeatureCollection.fromFeatures(
                    listOf(
                        Feature.fromGeometry(
                            Point.fromLngLat(21.0122, 52.2297) // Warszawa
                        )
                    )
                )

                // 3. Dodaj źródło danych
                val geoJsonSource = GeoJsonSource("marker-source", geoJson)
                style.addSource(geoJsonSource)

                // 4. Stwórz warstwę markerów
                val symbolLayer = SymbolLayer("marker-layer", "marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.2f)
                    )

                // 5. Dodaj warstwę na mapę
                style.addLayer(symbolLayer)

                // 6. Ustaw kamerę
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(52.2297, 21.0122))
                    .zoom(12.0)
                    .build()
            }
            map.addOnMapClickListener { point ->

                val screenPoint = map.projection.toScreenLocation(point)

                val features = map.queryRenderedFeatures(screenPoint, "marker-layer")

                if (features.isNotEmpty()) {
                    val context = mapView.context
                    Toast.makeText(context, "Kliknięto marker!", Toast.LENGTH_SHORT).show()
                    map.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(point.latitude, point.longitude))
                        .build()
                    true
                } else {
                    false
                }
            }
        }
        mapView
    })
}