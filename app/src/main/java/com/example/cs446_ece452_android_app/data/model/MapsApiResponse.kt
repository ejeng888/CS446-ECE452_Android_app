package com.example.cs446_ece452_android_app.data.model

import com.google.gson.annotations.SerializedName

data class LatLngResponse(
    @SerializedName("results") val results: List<LatLngResult>? = null
)

data class LatLngResult(
    @SerializedName("formatted_address") val address: String = "",
    @SerializedName("geometry") val geometry: Geometry? = null,
    @SerializedName("place_id") val placeId: String = ""
)

data class Geometry(
    @SerializedName("location") val location: LatLong? = null
)

data class LatLong(
    @SerializedName(value = "lat", alternate = ["latitude"]) val lat: Double = 0.0,
    @SerializedName(value = "lng", alternate = ["longitude"]) val lng: Double = 0.0
)


data class RouteResponse(
    @SerializedName("routes") val routes: List<Route>? = null
)

data class Route(
    @SerializedName("legs") val legs: List<Leg>? = null,
    @SerializedName("distanceMeters") val distance: Int = 0,
    @SerializedName("staticDuration") val duration: String = "",
    @SerializedName("polyline") val polyline: Polyline? = null,
    @SerializedName("viewport") val viewport: Viewport? = null,
    @SerializedName("optimizedIntermediateWaypointIndex") val order: List<Int>? = null
)

data class Polyline(
    @SerializedName("encodedPolyline") val encodedPolyline: String = ""
)

data class Viewport(
    @SerializedName("low") val low: LatLong? = null,
    @SerializedName("high") val high: LatLong? = null
)

data class Leg(
    @SerializedName("distanceMeters") val distance: Int = 0,
    @SerializedName("staticDuration") val duration: String = "",
    @SerializedName("polyline") val polyline: Polyline? = null,
    @SerializedName("startLocation") val start: Location? = null,
    @SerializedName("endLocation") val end: Location? = null,
    @SerializedName("steps") val steps: List<Step>? = null
)

data class Location(
    @SerializedName("latLng") val latLng: LatLong? = null
)

data class Step(
    @SerializedName("distanceMeters") val distance: Int = 0,
    @SerializedName("staticDuration") val duration: String = "",
    @SerializedName("polyline") val polyline: Polyline? = null,
    @SerializedName("startLocation") val start: Location? = null,
    @SerializedName("endLocation") val end: Location? = null,
    @SerializedName("navigationInstruction") val navInstruction: NavigationInstruction? = null,
    @SerializedName("transitDetails") val transitDetails: TransitDetails? = null,
    @SerializedName("travelMode") val mode: String = ""
)

data class NavigationInstruction(
    @SerializedName("maneuver") val maneuver: String = "",
    @SerializedName("instructions") val instruction: String = ""
)

data class TransitDetails(
    @SerializedName("stopDetails") val stopDetails: StopDetails? = null,
    @SerializedName("transitLine") val transitLine: TransitLine? = null,
    @SerializedName("stopCount") val stopCount: Int = 0
)

data class StopDetails(
    @SerializedName("arrivalStop") val arrivalStop: Stop? = null,
    @SerializedName("departureStop") val departureStop: Stop? = null
)

data class Stop(
    @SerializedName("name") val name: String = ""
)

data class TransitLine(
    @SerializedName("agencies") val agencies: List<Agency>? = null,
    @SerializedName("name") val name: String = "",
    @SerializedName("nameShort") val nameShort: String = "",
    @SerializedName("vehicle") val vehicle: Vehicle? = null
)

data class Agency(
    @SerializedName("name") val name: String = ""
)

data class Vehicle(
    @SerializedName("type") val type: String = ""
)
