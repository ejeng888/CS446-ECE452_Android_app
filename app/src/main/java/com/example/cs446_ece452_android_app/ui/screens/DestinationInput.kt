package com.example.cs446_ece452_android_app.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue5
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun DestinationInputScreen(navController: NavController, rc: RouteController, placesClient: PlacesClient) {

    val context = LocalContext.current
    fun addRoute(
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
        creatorEmail: String?,
        sharedEmails: List<String>,
        createdDate: String,
        lastModifiedDate: String
    ) {
        var emptyDestination = false
        for (destinationEntry in destinations) {
            if (destinationEntry.destination == "") {
                emptyDestination = true
                break
            }
        }

        if (creatorEmail == null) {
            toastHelper(context, "User not logged in")
        } else if (routeName == "") {
            toastHelper(context, "Please enter a Route Name")
        } else if (startDest.destination == "") {
            toastHelper(context, "Please enter a Starting Destination")
        } else if (endDest.destination == "") {
            toastHelper(context, "Please enter an Ending Destination")
        } else if (emptyDestination) {
            toastHelper(context, "Please fill in all the Stops")
        } else {
            Log.v("DestinationInput", "Passed Checks")

            rc.getRoute(routeName, location, maxCost, accessToCar, startDate, endDate, startDest, endDest, destinations, creatorEmail, sharedEmails, createdDate, lastModifiedDate)
            toastHelper(context, "Route Created")
            navController.navigate("Map")
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

            var routeName by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var maxCost by remember { mutableStateOf("") }
            var accessToCar by remember { mutableStateOf(false) }
            var startDate by remember { mutableStateOf(currentDateTime) }
            var endDate by remember { mutableStateOf(currentDateTime) }
            var startDest by remember { mutableStateOf(DestinationEntryStruct()) }
            var endDest by remember { mutableStateOf(DestinationEntryStruct()) }
            val destinations = remember { mutableStateListOf<DestinationEntryStruct>() }

            val creatorEmail = Firebase.auth.currentUser!!.email
            val sharedEmails: List<String> = emptyList()
            val createdDate = currentDateTime
            val lastModifiedDate = currentDateTime

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "New Route",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = DarkBlue
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                InputBox(labelVal = "Route Name*", placeHolder = "NY Grad Trip", routeName, valueChanged = { newValue -> routeName = newValue })
                InputBox(labelVal = "Location", placeHolder = "New York City, NY, US", location, valueChanged = { newValue -> location = newValue })
                Row {
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        InputBox(labelVal = "Max Cost", placeHolder = "$0.00", maxCost, valueChanged = { newValue -> maxCost = newValue })
                    }
                    CarSwitch(Switched = { newValue -> accessToCar = newValue })
                }
                DateTimeInputField(label = "Start Date", dateTime = startDate) { selectedDateTime ->
                    startDate = selectedDateTime
                }
                DateTimeInputField(label = "End Date ", dateTime = endDate) { selectedDateTime ->
                    endDate = selectedDateTime
                }
            }
            ProvideWindowInsets {
                Column(
                    modifier = Modifier.padding(start = 10.dp, end = 25.dp, top = 25.dp, bottom = 25.dp)
                ) {
                    DestinationEntry(
                        placesClient = placesClient,
                        timeChanged = {},
                        destinationChanged = { newValue ->
                            val updatedEntry = startDest.copy(destination = newValue)
                            startDest = updatedEntry
                        },
                        start = true
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
                            }
                        )
                    }
                    DestinationEntry(
                        placesClient = placesClient,
                        timeChanged = {},
                        destinationChanged = { newValue ->
                            val updatedEntry = endDest.copy(destination = newValue)
                            endDest = updatedEntry
                        },
                        end = true
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
                labelVal = "Calculate Route",
                navController = navController,
                function = {
                    addRoute(context, routeName, location, maxCost, accessToCar, startDate, endDate, startDest, endDest, destinations, creatorEmail, sharedEmails, createdDate, lastModifiedDate)
                }
            )
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun DateTimeInputField(label: String, dateTime: String, onDateTimeSelected: (String) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            color = Blue5
        )
        Text(
            text = dateTime,
            color = DarkBlue
        )
        DateTimePickerButton(label = "Pick") { selectedDateTime ->
            onDateTimeSelected(selectedDateTime)
        }
    }
}

@Composable
fun DateTimePickerButton(label: String, onDateTimeSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Button(
        onClick = {
            // Date Picker Dialog
            DatePickerDialog(context, { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                // Time Picker Dialog
                TimePickerDialog(context, { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)

                    // Format selected date and time
                    val dateTime = String.format(
                        Locale.US, "%02d/%02d/%04d %02d:%02d",
                        calendar.get(Calendar.DAY_OF_MONTH),
                        calendar.get(Calendar.MONTH) + 1,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE)
                    )
                    onDateTimeSelected(dateTime)
                    Toast.makeText(context, "Selected Date and Time: $dateTime", Toast.LENGTH_SHORT).show()
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()

            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        },
        colors = ButtonDefaults.buttonColors(containerColor = Blue2)
    ) {
        Text(
            text = label,
            color = Blue5
        )
    }
}