package com.example.cs446_ece452_android_app.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

data class DestinationEntryStruct(
    val destination : String = "",
    val timeSpent : String = "02:00"
)

/* data class RouteInformation(
    val routeName : String = "",
    val location : String = "",
    val maxCost : Double = 0.00,
    // val startDate : LocalDate = LocalDate.now(),
    // val endDate : LocalDate = LocalDate.now(),
    val destinations : Array<DestinationEntryStruct> = arrayOf<DestinationEntryStruct>()
) */

fun addRouteEntry(routeName : String, location : String, maxCost : String, startDate : String, endDate : String, destinations : SnapshotStateList<DestinationEntryStruct>) {
    val db = Firebase.firestore
    val dbEntry = hashMapOf(
        "routeName" to routeName,
        "location" to location,
        "maxCost" to maxCost,
        "startDate" to startDate,
        "endDate" to endDate,
        "destinations" to destinations
    )
    db.collection("routeEntries").add(dbEntry)
}
