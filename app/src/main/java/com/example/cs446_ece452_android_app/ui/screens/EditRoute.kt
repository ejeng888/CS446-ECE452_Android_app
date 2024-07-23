package com.example.cs446_ece452_android_app.ui.screens

import android.content.Context
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalContext
import com.example.cs446_ece452_android_app.data.RouteController
import com.example.cs446_ece452_android_app.data.model.DestinationEntryStruct
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.components.CarSwitch
import com.example.cs446_ece452_android_app.ui.components.DestinationEntry
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.components.toastHelper
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.libraries.places.api.net.PlacesClient
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun EditRouteScreen(navController: NavController, rc: RouteController, placesClient: PlacesClient) {
    val context = LocalContext.current

    fun editRoute(
        context: Context,
        routeName: String,
        location: String,
        maxCost: String,
        accessToCar: Boolean,
        startDate: String,
        endDate: String,
        startDest: DestinationEntryStruct,
        endDest: DestinationEntryStruct,
        destinations: List<DestinationEntryStruct>,
        lastModifiedDate: String
    ) {
        var emptyDestination = false
        for (destinationEntry in destinations) {
            if (destinationEntry.destination == "") {
                emptyDestination = true
                break
            }
        }

        if (routeName == "") {
            toastHelper(context, "Please enter a Route Name")
        } else if (startDest.destination == "") {
            toastHelper(context, "Please enter a Starting Destination")
        } else if (endDest.destination == "") {
            toastHelper(context, "Please enter an Ending Destination")
        } else if (emptyDestination) {
            toastHelper(context, "Please fill in all the Stops")
        } else {
            Log.v("DestinationInput", "Passed Checks")

            fun onSuccess() {
                toastHelper(context, "Route Updated")
                navController.navigate("Map")
            }

            fun onFailure(ex: Exception) {
                toastHelper(context, ex.toString().substringAfter("MSG: "))
            }

            rc.updateRoute(
                routeName,
                location,
                maxCost,
                accessToCar,
                startDate,
                endDate,
                startDest,
                endDest,
                destinations,
                lastModifiedDate,
                ::onSuccess,
                ::onFailure
            )
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }

    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue1)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            val currentDateTime = remember {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(calendar.time)
            }

            var routeName by remember { mutableStateOf(rc.routeEntry.routeName) }
            var location by remember { mutableStateOf(rc.routeEntry.location) }
            var maxCost by remember { mutableStateOf(rc.routeEntry.maxCost) }
            var accessToCar by remember { mutableStateOf(rc.routeEntry.accessToCar) }
            var startDate by remember { mutableStateOf(rc.routeEntry.startDate) }
            var endDate by remember { mutableStateOf(rc.routeEntry.endDate) }
            var startDest by remember { mutableStateOf(rc.routeEntry.startDest?.destination ?: "") }
            var endDest by remember { mutableStateOf(rc.routeEntry.endDest?.destination ?: "") }
            val destinations = remember { mutableStateListOf<DestinationEntryStruct>() }

            LaunchedEffect(rc.routeEntryLoaded) {
                destinations.clear()
                rc.routeEntry.destinations?.let { destinations.addAll(it) }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Edit Route",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = DarkBlue
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                InputBox(labelVal = "Route Name*", placeHolder = "NY Grad Trip", value = routeName, valueChanged = { newValue -> routeName = newValue })
                InputBox(labelVal = "Location", placeHolder = "New York City, NY, US", value = location, valueChanged = { newValue -> location = newValue })
                Row {
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        InputBox(labelVal = "Max Cost", placeHolder = "$0.00", value = maxCost, valueChanged = { newValue -> maxCost = newValue })
                    }
                    CarSwitch(value = accessToCar, onSwitch = { newValue -> accessToCar = newValue })
                }
                DateTimeInputField(label = "Start Date", dateTime = startDate) { selectedDateTime -> startDate = selectedDateTime }
                DateTimeInputField(label = "End Date ", dateTime = endDate) { selectedDateTime -> endDate = selectedDateTime }
            }
            ProvideWindowInsets {
                Column(
                    modifier = Modifier.padding(start = 10.dp, end = 25.dp, top = 25.dp, bottom = 25.dp)
                ) {
                    DestinationEntry(
                        placesClient = placesClient,
                        timeChanged = {},
                        destinationChanged = { newValue ->
                            startDest = newValue
                        },
                        start = true,
                        value = startDest
                    )
                    repeat(destinations.size) { index ->
                        DestinationEntry(
                            placesClient = placesClient,
                            timeChanged = { newValue ->
                                val updatedEntry = destinations[index].copy(timeSpent = newValue)
                                destinations[index] = updatedEntry
                            },
                            destinationChanged = { newValue ->
                                val updatedEntry = destinations[index].copy(destination = newValue)
                                destinations[index] = updatedEntry
                            },
                            value = destinations[index].destination,
                            time = destinations[index].timeSpent
                        )
                    }
                    DestinationEntry(
                        placesClient = placesClient,
                        timeChanged = {},
                        destinationChanged = { newValue ->
                            endDest = newValue
                        },
                        end = true,
                        value = endDest
                    )
                }
            }

            OutlinedButton(
                labelVal = "Add Destination",
                navController = navController,
                function = { destinations.add(DestinationEntryStruct()) }
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                labelVal = "Remove Destination",
                navController = navController,
                function = { if (destinations.size > 0) destinations.removeLast() }
            )
            Spacer(modifier = Modifier.height(30.dp))
            FilledButton(
                labelVal = "View Map",
                navController = navController,
                destination = "Map"
            )
            Spacer(modifier = Modifier.height(10.dp))
            FilledButton(
                labelVal = "Update Route",
                navController = navController,
                function = {
                    editRoute(
                        context,
                        routeName,
                        location,
                        maxCost,
                        accessToCar,
                        startDate,
                        endDate,
                        DestinationEntryStruct(destination = startDest),
                        DestinationEntryStruct(destination = endDest),
                        destinations,
                        currentDateTime
                    )
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}