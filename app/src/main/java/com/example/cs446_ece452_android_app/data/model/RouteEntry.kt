package com.example.cs446_ece452_android_app.data.model

data class RouteEntry(
    val routeName: String = "",
    val location: String = "",
    val maxCost: String = "",
    val accessToCar: Boolean = false,
    val startDate: String = "",
    val endDate: String = "",
    val startDest: DestinationEntryStruct? = null,
    val endDest: DestinationEntryStruct? = null,
    val destinations: List<DestinationEntryStruct>? = null,
    val creatorEmail: String = "",
    val sharedEmails: List<String>? = null,
    val createdDate: String = "",
    val lastModifiedDate: String = ""
)

data class DestinationEntryStruct(
    val destination: String = "",
    val timeSpent: String = "02:00"
)
