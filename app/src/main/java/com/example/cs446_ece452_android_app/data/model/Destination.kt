package com.example.cs446_ece452_android_app.data.model

import com.google.android.gms.maps.model.LatLng

data class Destination(
    var name: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var address: String = "",
    var description: String = "",
    var placeId: String = ""
)
