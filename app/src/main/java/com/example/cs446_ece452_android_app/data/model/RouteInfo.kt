package com.example.cs446_ece452_android_app.data.model

import com.google.android.gms.maps.model.LatLng

data class RouteInfo (
    var routeName: String,
    var cameraPos: LatLng,
    var cameraZoom: Float,
    var startLabel: String,
    var endLabel: String,
    var stopLabels: ArrayList<String>,
    var startDest: LatLng,
    var endDest: LatLng,
    var stopDests: ArrayList<LatLng>,
)