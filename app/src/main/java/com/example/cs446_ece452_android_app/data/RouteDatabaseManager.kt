package com.example.cs446_ece452_android_app.data

import com.example.cs446_ece452_android_app.data.model.RouteInfo
import com.example.cs446_ece452_android_app.data.model.RouteEntry
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

fun addRouteEntryToDb(dbEntry: RouteEntry, callback: (id: String) -> Unit) {
    val db = Firebase.firestore
    db.collection("routeEntries").add(dbEntry)
        .addOnSuccessListener { documentReference ->
            callback(documentReference.id)
        }
}

fun addRouteToDb(documentId: String, route: RouteInfo) {
    val db = Firebase.firestore
    db.collection("googleMapsRoutes").document(documentId).set(route)
}

fun getRouteEntryFromDb(documentId: String, callback: (route: RouteEntry) -> Unit) {
    val db = Firebase.firestore
    db.collection("routeEntries")
        .document(documentId)
        .get()
        .addOnSuccessListener { doc ->
            val result = doc.toObject(RouteEntry::class.java)
            if (result != null) {
                callback(result)
            }
        }
}

fun getRouteFromDb(documentId: String, callback: (route: RouteInfo) -> Unit) {
    val db = Firebase.firestore
    db.collection("googleMapsRoutes")
        .document(documentId)
        .get()
        .addOnSuccessListener { doc ->
            val result = doc.toObject(RouteInfo::class.java)
            if (result != null) {
                callback(result)
            }
        }
}
