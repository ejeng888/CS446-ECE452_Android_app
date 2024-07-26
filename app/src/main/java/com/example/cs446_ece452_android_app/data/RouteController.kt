package com.example.cs446_ece452_android_app.data

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.cs446_ece452_android_app.data.model.DestinationEntryStruct
import com.example.cs446_ece452_android_app.data.model.RouteInfo
import com.example.cs446_ece452_android_app.data.model.Route
import com.example.cs446_ece452_android_app.data.model.RouteEntry
import com.example.cs446_ece452_android_app.data.model.TravelMode
import java.util.concurrent.CompletableFuture

class RouteController(private val client: MapsApiClient) : ViewModel() {
    var routeEntry: RouteEntry = RouteEntry()
        private set
    var routeEntryLoaded by mutableStateOf(false)
        private set

    var routeInfo: RouteInfo = RouteInfo()
        private set
    var routeInfoLoaded by mutableStateOf(false)
        private set

    var routeId: String = ""
        private set

    fun getRoute(id: String) {
        routeId = id
        routeEntryLoaded = false
        routeInfoLoaded = false
        getRouteEntryFromDb(id) {
            routeEntry = it
            routeEntryLoaded = true
        }
        getRouteFromDb(id) {
            routeInfo = it
            routeInfoLoaded = true
        }
    }

    fun getRoute(
        routeName: String,
        location: String,
        maxCost: String,
        accessToCar: Boolean,
        startDate: String,
        endDate: String,
        startDest: DestinationEntryStruct,
        endDest: DestinationEntryStruct,
        destinations: List<DestinationEntryStruct>,
        creatorEmail: String,
        sharedEmails: List<String>,
        createdDate: String,
        lastModifiedDate: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        routeEntryLoaded = false
        routeInfoLoaded = false

        routeEntry = RouteEntry(routeName, location, maxCost, accessToCar, startDate, endDate, startDest, endDest, destinations, creatorEmail, sharedEmails, createdDate, lastModifiedDate)
        routeEntryLoaded = true

        try {
            calculateRoute()
            routeInfoLoaded = true

            addRouteEntryToDb(routeEntry) { id ->
                routeId = id
                writeRouteToDb(id, routeInfo)
            }

            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    fun updateRoute(
        routeName: String,
        location: String,
        maxCost: String,
        accessToCar: Boolean,
        startDate: String,
        endDate: String,
        startDest: DestinationEntryStruct,
        endDest: DestinationEntryStruct,
        destinations: List<DestinationEntryStruct>,
        lastModifiedDate: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        routeEntryLoaded = false
        routeInfoLoaded = false

        routeEntry = RouteEntry(
            routeName,
            location,
            maxCost,
            accessToCar,
            startDate,
            endDate,
            startDest,
            endDest,
            destinations,
            routeEntry.creatorEmail,
            routeEntry.sharedEmails,
            routeEntry.createdDate,
            lastModifiedDate
        )
        routeEntryLoaded = true

        try {
            calculateRoute()
            routeInfoLoaded = true

            writeRouteEntryToDb(routeId, routeEntry)
            writeRouteToDb(routeId, routeInfo)

            onSuccess()
        } catch (e: Exception) {
            onFailure(e)
        }
    }

    private fun calculateRoute() {
        val carAccess = routeEntry.accessToCar

        val startFuture = client.getDestination(routeEntry.startDest!!.destination)
        val endFuture = client.getDestination(routeEntry.endDest!!.destination)
        val destsFuture = routeEntry.destinations!!.map { client.getDestination(it.destination) }

        CompletableFuture.allOf(startFuture, endFuture, *destsFuture.toTypedArray()).thenCompose {
            routeInfo.startDest = startFuture.join()
            routeInfo.endDest = endFuture.join()
            routeInfo.stopDests = destsFuture.mapTo(arrayListOf()) { it.join() }

            client.getRoute(routeInfo.startDest!!, routeInfo.endDest!!, routeInfo.stopDests, TravelMode.CAR).thenCompose { route ->
                routeInfo.route = route

                if (route.order != null && route.order.size > 1)
                {
                    routeInfo.stopDests = route.order.map { routeInfo.stopDests!![it] }
                    routeEntry.destinations = route.order.map { routeEntry.destinations!![it] }
                }

                if (!carAccess) {
                    val routesFuture = mutableListOf<CompletableFuture<Route>>()
                    for (leg in route.legs ?: listOf()) {
                        val startLat = leg.start!!.latLng!!.lat
                        val startLng = leg.start.latLng!!.lng
                        val endLat = leg.end!!.latLng!!.lat
                        val endLng = leg.end.latLng!!.lng

                        val requestString = """
                        {
                            "origin": {
                                "location": {
                                    "latLng": {
                                        "latitude": $startLat,
                                        "longitude": $startLng
                                    }
                                }
                            },
                            "destination": {
                                "location": {
                                    "latLng": {
                                        "latitude": $endLat,
                                        "longitude": $endLng
                                    }
                                }
                            },
                            "travelMode": "TRANSIT"
                        }"""
                        routesFuture.add(client.getRoute(requestString))
                    }

                    CompletableFuture.allOf(*routesFuture.toTypedArray()).thenRun {
                        routeInfo.transitRoute = routesFuture.mapTo(arrayListOf()) { it.join() }
                    }.exceptionally {
                        throw Throwable("MSG: Unable to calculate transit route")
                    }
                } else {
                    CompletableFuture.completedFuture(null)
                }
            }.exceptionally {
                throw it
            }
        }.exceptionally {
            throw it
        }.join()
    }
}