package com.example.cs446_ece452_android_app.data.model

import com.google.android.gms.maps.model.LatLng

data class Destination(
    var name: String,
    var latLng: LatLng,
    var address: String = "",
    var description: String = "",
) {
    constructor(name: String, latLng: LatLng) : this(name, latLng, "", "")
    constructor(name: String, latLng: LatLng, address: String) : this(name, latLng, address, "")
}
