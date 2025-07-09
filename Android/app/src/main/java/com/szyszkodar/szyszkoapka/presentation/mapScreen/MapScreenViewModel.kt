package com.szyszkodar.szyszkoapka.presentation.mapScreen

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
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
            val features = state.value.bookpoints.map {
                Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
            }
            val geoJson = FeatureCollection.fromFeatures(features)

            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>("marker-source")

                if (source != null) {
                    source.setGeoJson(geoJson)
                }
            }
        }
    }


    fun createMap(mapView: MapView): MapView {
        mapView.getMapAsync { map ->
            map.setStyle("https://tiles.openfreemap.org/styles/liberty") { style ->
                style.addImage(
                    "marker-icon",
                    BitmapFactory.decodeResource(context.resources, R.drawable.marker)
                )

                val geoJsonSource = GeoJsonSource("marker-source", FeatureCollection.fromFeatures(emptyList()))
                style.addSource(geoJsonSource)

                val symbolLayer = SymbolLayer("marker-layer", "marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.2f)
                    )
                style.addLayer(symbolLayer)

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
        return mapView
    }
}