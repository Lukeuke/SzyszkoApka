package com.szyszkodar.szyszkomapka.presentation.mapScreen

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.annotation.Px
import androidx.compose.material3.MaterialTheme
import androidx.core.graphics.toColorInt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.szyszkodar.szyszkomapka.R
import com.szyszkodar.szyszkomapka.data.enums.AppMode
import com.szyszkodar.szyszkomapka.data.mappers.BookpointsMapper
import com.szyszkodar.szyszkomapka.data.permissions.LocalizationHandler
import com.szyszkodar.szyszkomapka.data.remote.body.CreateBookpointBody
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
import org.maplibre.android.maps.Style
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circleOpacity
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconColor
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
            val bookpoints = fetchBookpoints(query = GetBookpointsQuery(filters = listOf(BookpointsFilter.generic(FieldParam.APPROVED, OperatorParam.EQ, true))))
            bookpoints?.let { _state.update { it.copy(bookpoints = bookpoints) } }

            localizationHandler.observeUserLocation().collect { newLocation ->
                if (newLocation != null) {
                    _state.update { it.copy(userLocation = newLocation) }
                }
            }
        }
    }

    private suspend fun fetchBookpoints(query: GetBookpointsQuery): List<BookpointUI>? {
        val bookpointsMapper = BookpointsMapper()

        return when(val response = bookpointsRepository.getBookpoints(query)) {
            is Result.Success -> {
                response.data.data.map { el ->
                    bookpointsMapper.convert(el)
                }
            }
            is Result.Error -> {
                _state.update { it.copy(errorMessage = response.error.message) }
                null
            }
        }
    }

    fun updateMap(mapView: MapView){

        if (_state.value.appMode == AppMode.ADMIN){
            viewModelScope.launch {
                val unapprovedBookpoints = fetchBookpoints(query = GetBookpointsQuery(filters = listOf(
                    BookpointsFilter.generic(FieldParam.APPROVED, OperatorParam.EQ, false)
                )))

                unapprovedBookpoints?.let { _state.update { it.copy(unapprovedBookpoints = unapprovedBookpoints) }}
            }
        }

        mapView.getMapAsync { map ->
            // Create list of markers
            val features = state.value.bookpoints.map {
                val feature = Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
                feature.addStringProperty("data", Gson().toJson(it))

                feature
            }
            val geoJson = FeatureCollection.fromFeatures(features)

            // Create list of unapproved markers
            val unapprovedFeatures = state.value.unapprovedBookpoints.map {
                val feature = Feature.fromGeometry(Point.fromLngLat(it.longitude, it.latitude))
                feature.addStringProperty("data", Gson().toJson(it))

                feature
            }
            val unapprovedGeoJson = FeatureCollection.fromFeatures(unapprovedFeatures)


            // Edit map style
            map.getStyle { style ->
                val source = style.getSourceAs<GeoJsonSource>("marker-source")
                val unapprovedBookpointsSource = style.getSourceAs<GeoJsonSource>("unapproved-marker-source")

                // If there is any new marker - add it
                source?.setGeoJson(geoJson)

                // Add unapproved markers
                if (_state.value.appMode == AppMode.ADMIN) {
                    unapprovedBookpointsSource?.setGeoJson(unapprovedGeoJson)
                }

                // User localization
                val userLocalization = _state.value.userLocation
                if(userLocalization != null) {
                    val userFeature = Feature.fromGeometry(Point.fromLngLat(userLocalization.longitude, userLocalization.latitude))
                    val userSource = style.getSourceAs<GeoJsonSource>("user-location-source")
                    userSource?.setGeoJson(userFeature)
                }
            }
        }
        setCenterCoordinates(mapView)
    }

    fun refreshMap(mapView: MapView) {
        viewModelScope.launch {
            val approved = fetchBookpoints(query = GetBookpointsQuery(filters = listOf(BookpointsFilter.generic(FieldParam.APPROVED, OperatorParam.EQ, true))))
            val unapproved = fetchBookpoints(query = GetBookpointsQuery(filters = listOf(BookpointsFilter.generic(FieldParam.APPROVED, OperatorParam.EQ, false))))

            if (approved != null && unapproved != null) {
                _state.update { it.copy(bookpoints = approved, unapprovedBookpoints = unapproved) }
                updateMap(mapView)
            }
        }
    }

    fun createMap(mapView: MapView): MapView {
        mapView.getMapAsync { map ->
            // Setting style of the map
            map.setStyle(Style.Builder().fromUri("asset://map-style.json")) { style ->
                style.addImage(
                    "marker-icon",
                    BitmapFactory.decodeResource(context.resources, R.drawable.marker),
                    true
                )

                // Markers
                val geoJsonSource = GeoJsonSource("marker-source", FeatureCollection.fromFeatures(emptyList()))
                style.addSource(geoJsonSource)

                // Unaccepted markers
                val unapprovedMarkersSource = GeoJsonSource("unapproved-marker-source", FeatureCollection.fromFeatures(emptyList()))
                style.addSource(unapprovedMarkersSource)

                // User location
                val locationSource = GeoJsonSource("user-location-source")
                style.addSource(locationSource)

                // Markers layer
                val markersLayer = SymbolLayer("marker-layer", "marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.3f),
                        iconColor("#b07b4f".toColorInt())
                    )
                style.addLayer(markersLayer)

                val unapprovedMarkersLayer = SymbolLayer("unapproved-markers-layer", "unapproved-marker-source")
                    .withProperties(
                        iconImage("marker-icon"),
                        iconAllowOverlap(true),
                        iconIgnorePlacement(true),
                        iconSize(0.3f),
                        iconColor("#fc6423".toColorInt())
                    )
                style.addLayer(unapprovedMarkersLayer)

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

    fun setCompassMargins(mapView: MapView, @Px topPadding: Int) {
        mapView.getMapAsync { map ->
            map.uiSettings.setCompassMargins(0, topPadding, 0, 0)
        }
    }

    private fun markerClickListener(map: MapLibreMap) {
        map.addOnMapClickListener { point ->
            // Get tap location
            val screenPoint = map.projection.toScreenLocation(point)

            // Give a list of clicked markers
            val features = map.queryRenderedFeatures(screenPoint, "marker-layer", "unapproved-markers-layer")

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

                    toggleBookpointVisibility(bookpoint)

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

    private fun changeCameraPosition(mapLibreMap: MapLibreMap, targetLatLng: LatLng, targetZoom: Double = 12.0) {
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

    fun mapViewCameraPositionChange(mapView: MapView, targetLatLng: LatLng, targetZoom: Double = 12.0) {
        mapView.getMapAsync {
            changeCameraPosition(
                mapLibreMap = it,
                targetLatLng = targetLatLng,
                targetZoom = targetZoom
            )
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

    fun toggleBookpointVisibility(bookpoint: BookpointUI? = null) {
        if (bookpoint == null) {
            _state.update { it.copy(chosenBookpoint = null) }
        } else {
            _state.update { it.copy(chosenBookpoint = bookpoint) }
        }
    }

    fun setErrorMessageShownToTrue(){
        _state.update { it.copy(errorShown = true) }
    }

    fun changeAppMode(mode: AppMode) {
        _state.update { it.copy(appMode = mode) }
    }

    private fun setCenterCoordinates(mapView: MapView) {
        mapView.getMapAsync{ map ->
            val centerScreenPoint = android.graphics.PointF(
                map.width / 2f,
                map.height / 2f
            )

            Log.d("koń3", centerScreenPoint.toString())

            val centerLatLng = map.projection.fromScreenLocation(centerScreenPoint)
            _state.update { it.copy(centerLatLng = centerLatLng) }
        }


    }

    suspend fun addBookpoint(name: String, description: String, onSuccess: () -> Unit, onSError: (String) -> Unit){
        val body = CreateBookpointBody(
            lat = _state.value.centerLatLng.latitude.toFloat(),
            lon = _state.value.centerLatLng.longitude.toFloat(),
            title = name,
            description = description
        )

        when(val response = bookpointsRepository.createBookpoint(body)) {
            is Result.Success -> onSuccess()
            is Result.Error -> onSError(response.error.message)
        }
    }

    fun setBearerToken(token: String) {
        _state.update { it.copy(bearerToken = token) }
    }

}