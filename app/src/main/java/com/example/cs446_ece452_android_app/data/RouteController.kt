package com.example.cs446_ece452_android_app.data

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.cs446_ece452_android_app.data.model.GoogleRouteInfo
import com.example.cs446_ece452_android_app.data.model.Leg
import com.example.cs446_ece452_android_app.data.model.Route
import com.example.cs446_ece452_android_app.data.model.TravelMode
import kotlinx.coroutines.runBlocking
import java.util.concurrent.CompletableFuture

class RouteController(private val client: MapsApiClient) : ViewModel() {
    private lateinit var routeName: String
    private lateinit var location: String
    private lateinit var maxCost: String
    private var accessToCar: Boolean = false
    private lateinit var startDate: String
    private lateinit var endDate: String
    private lateinit var startDest: DestinationEntryStruct
    private lateinit var endDest: DestinationEntryStruct
    private var destinations: List<DestinationEntryStruct>? = null

    var routeInfo: GoogleRouteInfo = GoogleRouteInfo()
        private set
    var dataLoaded by mutableStateOf(false)
        private set

    fun getRoute(
        routeName: String,
        location: String,
        maxCost: String,
        accessToCar: Boolean,
        startDate: String,
        endDate: String,
        startDest: DestinationEntryStruct,
        endDest: DestinationEntryStruct,
        destinations: List<DestinationEntryStruct>
    ) {
        dataLoaded = false

        this.routeName = routeName
        this.location = location
        this.maxCost = maxCost
        this.accessToCar = accessToCar
        this.startDate = startDate
        this.endDate = endDate
        this.startDest = startDest
        this.endDest = endDest
        this.destinations = destinations

        saveRouteEntry()
        calculateRoute()
    }

    private fun saveRouteEntry() {
        addRouteEntry(routeName, location, maxCost, accessToCar, startDate, endDate, startDest, endDest, destinations!!)
    }

    fun combineRoutes(routeResponses: List<Route>): Route{

        val combinedLegs = mutableListOf<Leg>()
        var totalDistance = 0
        //var totalDuration = 0
        for (route in routeResponses) {
            combinedLegs.addAll(route.legs)
            totalDistance += route.distance
        }
        val firstLeg = routeResponses[0]
        val combinedRoute = Route(
            legs = combinedLegs,
            distance = totalDistance,
            duration = firstLeg.duration,
            polyline = firstLeg.polyline,
            viewport = firstLeg.viewport,
            order = firstLeg.order
        )
        return combinedRoute
    }

    fun calculateRoute() {
        val carAccess = accessToCar
        val startFuture = client.getDestination(startDest.destination)
        val endFuture = client.getDestination(endDest.destination)

        val destsFuture = destinations!!.map { client.getDestination(it.destination) }

        CompletableFuture.allOf(startFuture, endFuture, *destsFuture.toTypedArray()).thenRun {
            routeInfo.startDest = startFuture.join()
            routeInfo.endDest = endFuture.join()

            routeInfo.stopDests = destsFuture.mapTo(arrayListOf()) { it.join() }
            if(carAccess){
                routeInfo.route = client.getRoute(routeInfo.startDest!!, routeInfo.endDest!!, routeInfo.stopDests, TravelMode.CAR).join()
            }
            else{
                routeInfo.route = client.getRoute(routeInfo.startDest!!, routeInfo.endDest!!, routeInfo.stopDests, TravelMode.PUBLIC_TRANSPORTATION).join()
                //Might need to wait for this function to finish
                //At this point, routeInfo.route will contain the optimized car drive route between stops
                //Now I look at every 2 stops in routeInfo, and get response, then combine the response
                val routesList = mutableListOf<Route>()
                for (leg in routeInfo.route?.legs?: listOf()){
                    val startLat = leg.start.latLng.lat
                    val startLng = leg.start.latLng.lng
                    val endLat = leg.end.latLng.lat
                    val endLng = leg.end.latLng.lng

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
                    val tempRouteInfo = client.transitRequestString(requestString).join()
                    routesList.add(tempRouteInfo)
                }

                val combinedRoute = combineRoutes(routesList)
                routeInfo.route = combinedRoute
            }

        }.thenRun{
            dataLoaded = true
        }
    }
}