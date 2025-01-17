package com.example.cs446_ece452_android_app.data.model

data class StepData(
    val distance: Int,
    val instruction: String,
    val lat: Double,
    val lng: Double,
    val stopCount: Int,
    val departureStopName: String,
    val arrivalStopName: String,
    val transitLineVehicle: String,
    val intermediateStop: Boolean
)