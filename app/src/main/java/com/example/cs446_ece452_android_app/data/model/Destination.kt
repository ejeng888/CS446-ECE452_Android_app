package com.example.cs446_ece452_android_app.data.model

data class Destination(
    var name: String = "",
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var address: String = "",
    var placeId: String = "",
    var description: String = ""
)
