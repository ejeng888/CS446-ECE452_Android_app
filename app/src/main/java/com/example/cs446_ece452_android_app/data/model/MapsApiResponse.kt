package com.example.cs446_ece452_android_app.data.model

import com.google.gson.annotations.SerializedName

data class LatLngResult(
    @SerializedName("formatted_address") val address: String,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("place_id") val placeId: String
)

data class Geometry(
    @SerializedName("location") val location: LatLong
)

data class LatLong(
    @SerializedName(value = "lat", alternate = ["latitude"]) val lat: Double,
    @SerializedName(value = "lng", alternate = ["longitude"]) val lng: Double
)

data class LatLngResponse(
    @SerializedName("results") val results: List<LatLngResult>
)


data class PlaceDetailsResponse(
    @SerializedName("result") val result: PlaceResult,
)

data class PlaceResult(
//    @SerializedName("formatted_address") val address: String,
//    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("name") val name: String
)


data class RouteResponse(
    @SerializedName("routes") val routes: List<Route>
)

data class Route(
    @SerializedName("legs") val legs: List<Leg>,
    @SerializedName("distanceMeters") val distance: Int,
    @SerializedName("staticDuration") val duration: String,
    @SerializedName("polyline") val polyline: Polyline,
    @SerializedName("viewport") val viewport: Viewport,
    @SerializedName("optimizedIntermediateWaypointIndex") val order: List<Int>
)

data class Polyline(
    @SerializedName("encodedPolyline") val encodedPolyline: String
)

data class Viewport(
    @SerializedName("low") val low: LatLong,
    @SerializedName("high") val high: LatLong
)

data class Leg(
    @SerializedName("distanceMeters") val distance: Int,
    @SerializedName("staticDuration") val duration: String,
    @SerializedName("polyline") val polyline: Polyline,
    @SerializedName("startLocation") val start: Location,
    @SerializedName("endLocation") val end: Location,
    @SerializedName("steps") val steps: List<Step>
)

data class Location (
    @SerializedName("latLng") val latLng: LatLong
)

data class Step(
    @SerializedName("distanceMeters") val distance: Int,
    @SerializedName("staticDuration") val duration: String,
    @SerializedName("polyline") val polyline: Polyline,
    @SerializedName("startLocation") val start: Location,
    @SerializedName("endLocation") val end: Location,
    @SerializedName("navigationInstruction") val navInstruction: NavigationInstruction,
    @SerializedName("travelMode") val mode: String
)

data class NavigationInstruction(
    @SerializedName("maneuver") val maneuver: String,
    @SerializedName("instructions") val instruction: String
)
