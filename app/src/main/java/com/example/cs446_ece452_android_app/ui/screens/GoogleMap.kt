package com.example.cs446_ece452_android_app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.R
import com.example.cs446_ece452_android_app.data.RouteCalculator
import com.example.cs446_ece452_android_app.data.model.Destination
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PinConfig
import com.google.maps.android.compose.AdvancedMarker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun MapScreen(navController: NavController, rc: RouteCalculator) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val mapId = stringResource(R.string.map_id)

    var cameraPosition = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(35.0048, 135.7685), 12.5f)
    }
    
    LaunchedEffect(rc.dataLoaded) {
        if (rc.dataLoaded)
            cameraPosition.position = CameraPosition.fromLatLngZoom(rc.routeInfo.cameraPos!!, rc.routeInfo.cameraZoom)
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues), // Use the provided padding values
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPosition,
                googleMapOptionsFactory = { GoogleMapOptions().mapId(mapId) },
                onMapLoaded = { isMapLoaded = true },
                content = {
                    if (rc.dataLoaded) {
                        MapContent(
                            start = rc.routeInfo.startDest!!,
                            end = rc.routeInfo.endDest!!,
                            stops = rc.routeInfo.stopDests,
                            order = rc.routeInfo.route!!.order,
                            poly = rc.routeInfo.route!!.polyline.encodedPolyline
                        )
                    }
                }
            )
        }
    }
}

@Composable
@GoogleMapComposable
fun MapContent(start: Destination, end: Destination, stops: ArrayList<Destination>?, order: List<Int>?, poly: String) {
    StartEndMarker(start)
    if (start.address != end.address)
        StartEndMarker(end)
    if (stops != null && order != null)
        StopMarkers(stops, order)
    Route(poly)
}

@Composable
@GoogleMapComposable
fun StartEndMarker(destination: Destination) {
    Marker(
        state = rememberMarkerState(position = LatLng(destination.lat, destination.lng)),
        title = destination.name
    )
}

@Composable
@GoogleMapComposable
fun StopMarkers(stops: ArrayList<Destination>, order: List<Int>) {
    stops.forEachIndexed { i, stop ->
        AdvancedMarker(
            state = rememberMarkerState(position = LatLng(stop.lat, stop.lng)),
            title = stop.name,
            pinConfig = configPin((order.indexOf(i) + 1).toString())
        )
    }
}

private fun configPin(s: String): PinConfig {
    return with(PinConfig.builder()) {
        setGlyph(PinConfig.Glyph(s, android.graphics.Color.WHITE))
        setBackgroundColor(android.graphics.Color.MAGENTA)
        setBorderColor(android.graphics.Color.MAGENTA)
        build()
    }
}

@Composable
@GoogleMapComposable
fun Route(poly: String) {
    val decoded = decodePolyline(poly)

    Polyline(
        points = decoded,
        color = Color.Blue
    )
}

private fun decodePolyline(encoded: String): List<LatLng> {
    val poly = ArrayList<LatLng>()
    var index = 0
    val len = encoded.length
    var lat = 0
    var lng = 0

    while (index < len) {
        var b: Int
        var shift = 0
        var result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lat += dlat

        shift = 0
        result = 0
        do {
            b = encoded[index++].code - 63
            result = result or (b and 0x1f shl shift)
            shift += 5
        } while (b >= 0x20)
        val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
        lng += dlng

        val finalLat = lat / 1E5
        val finalLng = lng / 1E5
        poly.add(LatLng(finalLat, finalLng))
    }

    return poly
}