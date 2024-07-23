package com.example.cs446_ece452_android_app.data.model

data class RouteEntry(
    var routeName: String = "",
    var location: String = "",
    var maxCost: String = "",
    var accessToCar: Boolean = false,
    var startDate: String = "",
    var endDate: String = "",
    var startDest: DestinationEntryStruct? = null,
    var endDest: DestinationEntryStruct? = null,
    var destinations: List<DestinationEntryStruct>? = null,
    var creatorEmail: String = "",
    var sharedEmails: List<String>? = null,
    var createdDate: String = "",
    var lastModifiedDate: String = ""
)

data class DestinationEntryStruct(
    var destination: String = "",
    var timeSpent: String = "02:00"
)
