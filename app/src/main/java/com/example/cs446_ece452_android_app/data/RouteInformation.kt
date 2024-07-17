package com.example.cs446_ece452_android_app.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class DestinationEntryStruct(
    val destination: String = "",
    val timeSpent: String = "02:00"
)

/* data class RouteInformation(
    val routeName : String = "",
    val location : String = "",
    val maxCost : Double = 0.00,
    // val startDate : LocalDate = LocalDate.now(),
    // val endDate : LocalDate = LocalDate.now(),
    val destinations : Array<DestinationEntryStruct> = arrayOf<DestinationEntryStruct>()
) */

fun addRouteEntry(
    routeName: String,
    location: String,
    maxCost: String,
    accessToCar: Boolean,
    startDate: String,
    endDate: String,
    startDestination: DestinationEntryStruct,
    endDestination: DestinationEntryStruct,
    destinations: List<DestinationEntryStruct>
) {
    val db = Firebase.firestore
    val dbEntry = hashMapOf(
        "routeName" to routeName,
        "location" to location,
        "maxCost" to maxCost,
        "accessToCar" to accessToCar,
        "startDate" to startDate,
        "endDate" to endDate,
        "startDestination" to startDestination,
        "endDestination" to endDestination,
        "destinations" to destinations
    )
    db.collection("routeEntries").add(dbEntry)
}
