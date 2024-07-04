package com.example.cs446_ece452_android_app.ui.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.data.DestinationEntryStruct
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.data.addRouteEntry
import com.example.cs446_ece452_android_app.ui.components.CarSwitch
import com.example.cs446_ece452_android_app.ui.components.DestinationEntry
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton

@Composable
fun DestinationInputScreen(navController: NavController) {
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
        ) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "New Route",
                fontSize = 32.sp,
                textAlign = TextAlign.Center,
                color = DarkBlue
            )

            var routeName by remember { mutableStateOf("") }
            var location by remember { mutableStateOf("") }
            var maxCost by remember { mutableStateOf("") }
            var accessToCar by remember { mutableStateOf(false) }
            var startDate by remember { mutableStateOf("") }
            var endDate by remember { mutableStateOf("") }
            val destinations = remember { mutableStateListOf<DestinationEntryStruct>(DestinationEntryStruct(), DestinationEntryStruct())}

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(horizontal = 25.dp)
            ) {
                InputBox(labelVal = "Route Name", placeHolder = "NY Grad Trip", valueChanged = {newValue -> routeName = newValue})
                InputBox(labelVal = "Location", placeHolder = "New York City, NY, US", valueChanged = {newValue -> location = newValue})
                Row {
                    Box(modifier = Modifier.fillMaxWidth(0.5f)) {
                        InputBox(labelVal = "Max Cost", placeHolder = "$0.00", valueChanged = { newValue -> maxCost = newValue })
                    }
                        CarSwitch(Switched = { newValue -> accessToCar = newValue })
                }

                InputBox(labelVal = "Start Date", valueChanged = {newValue -> startDate = newValue})
                InputBox(labelVal = "End Date", valueChanged = {newValue -> endDate = newValue})
            }

            Column(
                modifier = Modifier.padding(start = 10.dp, end=25.dp, top=25.dp, bottom=25.dp)
            ) {
                repeat(destinations.size) { index ->
                    DestinationEntry(
                        timeChanged = {newValue ->
                            val updatedEntry = destinations[index].copy(timeSpent = newValue)
                            destinations[index] = updatedEntry},
                        destinationChanged = {newValue ->
                            val updatedEntry = destinations[index].copy(destination = newValue)
                            destinations[index] = updatedEntry},
                        start = (index == 0),
                        end = (index == destinations.size - 1)
                        )
                }
            }

            OutlinedButton(
                labelVal = "Add Destination",
                navController = navController,
                function = {destinations.add(DestinationEntryStruct())}
            )

            FilledButton(
                labelVal = "Calculate Route",
                navController = navController,
                destination = "Map",
                function = {addRouteEntry(routeName, location, maxCost, accessToCar, startDate, endDate, destinations)}
            )

        }
    }
}

@Preview
@Composable
fun DestinationInputScreenPreview() {
    DestinationInputScreen(rememberNavController())
}