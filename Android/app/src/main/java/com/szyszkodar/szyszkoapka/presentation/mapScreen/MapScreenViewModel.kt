package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szyszkodar.szyszkoapka.R
import com.szyszkodar.szyszkoapka.data.mappers.BookpointsMapper
import com.szyszkodar.szyszkoapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkoapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkoapka.domain.errorHandling.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.maps.MapLibreMap
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
import javax.inject.Inject

@HiltViewModel
class MapScreenViewModel @Inject  constructor(
    private val bookpointsRepository: BookpointsRepository,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _state = MutableStateFlow(MapScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = MapScreenState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        fetchBookpoints()
    }

    fun fetchBookpoints() {
        val bookpointsMapper = BookpointsMapper()

        viewModelScope.launch {
            val query = GetBookpointsQuery()

            when(val response = bookpointsRepository.fetchData(query = query)) {
                is Result.Success -> {
                    _state.update { it.copy(bookpoints = response.data.data.map { el ->
                        bookpointsMapper.convert(el)
                    }, errorMessage = null) }
                }
                is Result.Error -> {
                    _state.update { it.copy(errorMessage = response.error.message) }
                }
            }
        }
    }

    fun updateMap(mapView: MapView) {
        mapView.getMapAsync { map ->
            // Create list of markers
            val features = state.value.bookpoints.map {
                Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
            }
            val geoJson = FeatureCollection.fromFeatures(features)

            // Edit map style
            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>("marker-source")

                // If there is any new marker - add it
                if (source != null) {
                    source.setGeoJson(geoJson)
                }
            }
        }
    }


    fun createMap(mapView: MapView): MapView {
        mapView.getMapAsync { map ->
            // Setting style of the map
            map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
                style.addImage(
                    "marker-icon",
                    BitmapFactory.decodeResource(context.resources, R.drawable.marker)
                )

                // Markers
                val geoJsonSource = GeoJsonSource("marker-source", FeatureCollection.fromFeatures(emptyList()))
                style.addSource(geoJsonSource)

                // Markers layer
                val symbolLayer = SymbolLayer("marker-layer", "marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.2f)
                    )
                style.addLayer(symbolLayer)

                // Starting camera position TODO: get user location
                map.cameraPosition = CameraPosition.Builder()
                    .target(LatLng(52.2297, 21.0122))
                    .zoom(12.0)
                    .build()
            }

            // Create listener to marker clicks
            markerClickListener(map)
        }

        return mapView
    }

    private fun markerClickListener(map: MapLibreMap) {
        map.addOnMapClickListener { point ->
            // Get tap location
            val screenPoint = map.projection.toScreenLocation(point)

            // Give a list of clicked markers
            val features = map.queryRenderedFeatures(screenPoint, "marker-layer")

            // Check if any marker was clicked
            if (features.isNotEmpty()) {
                // Give the first marker on the list
                val clickedFeature = features.first()
                val geometry = clickedFeature.geometry()

                if (geometry is Point) {
                    val markerLatLng = LatLng(geometry.latitude(), geometry.longitude())

                    // Make toase TODO: move it to ScreenView
                    Toast.makeText(context, "Kliknięto marker!", Toast.LENGTH_SHORT).show()

                    // Setup new camera position
                    changeCameraPosition(map, markerLatLng)
                }

                // Marker was clicked
                true
            } else {
                // Marker was not clicked
                false
            }
        }
    }

    private fun changeCameraPosition(mapLibreMap: MapLibreMap ,targetLatLng: LatLng, targetZoom: Double = 12.0) {
        mapLibreMap.let { map ->
            // Create builder for target camera position
            val cameraPositionBuilder = CameraPosition.Builder()
                .target(targetLatLng)
                .zoom(targetZoom)

            // Build it
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPositionBuilder.build())

            // Animate
            map.animateCamera(cameraUpdate, 1500)
        }
    }
}