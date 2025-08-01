package com.szyszkodar.szyszkomapka.presentation.mapScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.szyszkodar.szyszkomapka.R
import com.szyszkodar.szyszkomapka.data.mappers.BookpointsMapper
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.data.remote.filter.BookpointsFilter
import com.szyszkodar.szyszkomapka.data.remote.query.GetBookpointsQuery
import com.szyszkodar.szyszkomapka.data.repository.BookpointsRepository
import com.szyszkodar.szyszkomapka.data.uiClasses.BookpointUI
import com.szyszkodar.szyszkomapka.domain.errorHandling.Result
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.FieldParam
import com.szyszkodar.szyszkomapka.domain.remote.filterParams.OperatorParam
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
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circleOpacity
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
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
    private val localizationHandler: LocalizationHandler,
    @ApplicationContext private val context: Context
): ViewModel() {
    private val _state = MutableStateFlow(MapScreenState())
    val state = _state.stateIn(
        scope = viewModelScope,
        initialValue = MapScreenState(),
        started = SharingStarted.WhileSubscribed(5000)
    )

    init {
        viewModelScope.launch {
            getUserLocation()
            fetchBookpoints()

            localizationHandler.observeUserLocation().collect { newLocation ->
                if (newLocation != null) {
                    _state.update { it.copy(userLocation = newLocation) }
                }
            }
        }
    }

    private fun fetchBookpoints() {
        val bookpointsMapper = BookpointsMapper()

        viewModelScope.launch {
            // Fetch only approved bookpoints
            val query = GetBookpointsQuery(
                filters = listOf(
                    BookpointsFilter.generic(FieldParam.APPROVED, OperatorParam.EQ, true)
                )
            )

            when(val response = bookpointsRepository.getBookpoints(query)) {
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

    fun updateMap(mapView: MapView){
        mapView.getMapAsync { map ->
            // Create list of markers
            val features = state.value.bookpoints.map {
                val feature = Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
                feature.addStringProperty("data", Gson().toJson(it))

                feature
            }
            val geoJson = FeatureCollection.fromFeatures(features)

            // Edit map style
            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>("marker-source")

                // If there is any new marker - add it
                source?.setGeoJson(geoJson)

                // User localization
                val userLocalization = _state.value.userLocation
                if(userLocalization != null) {
                    val userFeature = Feature.fromGeometry(Point.fromLngLat(userLocalization.longitude, userLocalization.latitude))
                    val userSource = style.getSourceAs<GeoJsonSource>("user-location-source")
                    userSource?.setGeoJson(userFeature)
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

                // User location
                val locationSource = GeoJsonSource("user-location-source")
                style.addSource(locationSource)

                // Markers layer
                val markersLayer = SymbolLayer("marker-layer", "marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.2f)
                    )
                style.addLayer(markersLayer)

                // User location layer
                val circleLayer = CircleLayer("user-location-layer", "user-location-source").withProperties(
                    circleColor("#1E90FF".toColorInt()),
                    circleRadius(6.0f),
                    circleOpacity(0.8f),
                    circleStrokeColor(Color.WHITE),
                    circleStrokeWidth(2.0f)
                )
                style.addLayer(circleLayer)

                // Starting camera position TODO: get user location
                map.cameraPosition = CameraPosition.Builder()
                    .target(
                        location = state.value.userLocation ?: LatLng(52.2297, 21.0122)
                    )
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

                // Get bookpoint data
                val data = clickedFeature.getStringProperty("data")
                val bookpoint = Gson().fromJson(data, BookpointUI::class.java)

                if (geometry is Point) {
                    val markerLatLng = LatLng(geometry.latitude(), geometry.longitude())

                    // Make toast TODO: move it to ScreenView
                    Toast.makeText(context, "Kliknięto ${bookpoint.id}", Toast.LENGTH_SHORT).show()

                    _state.update { it.copy(bookpointInfoVisible = true, chosenBookpoint = bookpoint) }

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

    fun onLocalizationPermitted(mapView: MapView) {
        mapView.getMapAsync { map ->
            viewModelScope.launch {
                val location = getUserLocation()
                _state.update { it.copy(userLocation = location) }
                changeCameraPosition(map, location)
                updateMap(mapView)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getUserLocation(): LatLng {
        val userLocation = localizationHandler.getUserLocalization()
        _state.update { it.copy(userLocation = userLocation) }

        return userLocation ?: LatLng(52.2297, 21.0122)
    }

    fun toggleBookpointVisibility() {
        _state.update { it.copy(bookpointInfoVisible = !it.bookpointInfoVisible) }
    }

    fun setErrorMessageShownToTrue(){
        _state.update { it.copy(errorShown = true) }
    }
}