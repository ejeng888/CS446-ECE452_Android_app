package com.example.cs446_ece452_android_app.data

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class RouteInformation(
    val routeName : String = "",
    val location : String = "",
    val maxCost : Double = 0.00,
    // val startDate : LocalDate = LocalDate.now(),
    // val endDate : LocalDate = LocalDate.now(),
    val destinations : Array<String> = arrayOf<String>()
)

fun addRouteEntry(routeName : String, location : String, maxCost : String) {
    val db = Firebase.firestore
    val dbEntry = hashMapOf(
        "routeName" to routeName,
        "location" to location,
        "maxCost" to maxCost
    )
    db.collection("routeEntries").add(dbEntry)
}
