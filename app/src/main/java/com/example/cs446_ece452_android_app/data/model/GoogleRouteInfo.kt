package com.example.cs446_ece452_android_app.data.model

import com.google.android.gms.maps.model.LatLng

data class GoogleRouteInfo (
    var routeName: String = "",
    var location: String = "",
    var maxCost: String = "",
    var accessToCar: Boolean = false,
    var startDate: String = "",
    var endDate: String = "",


    var cameraPos: LatLng? = null,
    var cameraZoom: Float = 0f,

    var startDest: Destination? = null,
    var endDest: Destination? = null,
    var stopDests: ArrayList<Destination>? = null,

    var route: Route? = null
)