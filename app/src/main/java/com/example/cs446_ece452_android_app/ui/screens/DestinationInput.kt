package com.example.cs446_ece452_android_app.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
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
import android.widget.Toast
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.data.DestinationEntryStruct
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.data.addRouteEntry
import com.example.cs446_ece452_android_app.data.calculatePath
import com.example.cs446_ece452_android_app.ui.components.CarSwitch
import com.example.cs446_ece452_android_app.ui.components.DestinationEntry
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import java.text.SimpleDateFormat
import java.util.Locale

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
            val currentDateTime = remember {
                val calendar = Calendar.getInstance()
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                sdf.format(calendar.time)
            }

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
            var startDate by remember { mutableStateOf(currentDateTime) }
            var endDate by remember { mutableStateOf(currentDateTime) }
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

                DateTimeInputField(label = "Start", dateTime = startDate) { selectedDateTime ->
                    startDate = selectedDateTime
                }

                DateTimeInputField(label = "End", dateTime = endDate) { selectedDateTime ->
                    endDate = selectedDateTime
                }
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
                function = {
                    val optimizedPathList = calculatePath(destinations)
                    addRouteEntry(routeName, location, maxCost, accessToCar, startDate, endDate, destinations)
                }
            )

        }
    }
}

@Preview
@Composable
fun DestinationInputScreenPreview() {
    DestinationInputScreen(rememberNavController())
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
            text = label
        )
        Text(
            text = dateTime
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

    Button(onClick = {
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
                val dateTime = String.format(Locale.US, "%02d/%02d/%04d %02d:%02d",
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
    }) {
        Text(label)
    }
}