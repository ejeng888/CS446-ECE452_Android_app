package com.example.cs446_ece452_android_app.data

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
    startDest: DestinationEntryStruct,
    endDest: DestinationEntryStruct,
    destinations: List<DestinationEntryStruct>,
    creatorEmail : String,
    sharedEmails : List<String>
) {
    val db = Firebase.firestore
    val dbEntry = hashMapOf(
        "routeName" to routeName,
        "location" to location,
        "maxCost" to maxCost,
        "accessToCar" to accessToCar,
        "startDate" to startDate,
        "endDate" to endDate,
        "startDest" to startDest,
        "endDest" to endDest,
        "destinations" to destinations,
        "creatorEmail" to creatorEmail,
        "sharedEmails" to sharedEmails
    )
    db.collection("routeEntries").add(dbEntry)
}
