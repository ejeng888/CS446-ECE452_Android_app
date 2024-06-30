package com.example.cs446_ece452_android_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.data.addRouteEntry

@Composable
fun DestinationInputScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }

    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue1)
                .padding(paddingValues)
                .padding(horizontal = 25.dp)
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

            InputBox(labelVal = "Route Name", placeHolder = "NY Grad Trip", valueChanged = {newValue -> routeName = newValue})
            InputBox(labelVal = "Location", placeHolder = "New York City, NY, US", valueChanged = {newValue -> location = newValue})
            InputBox(labelVal = "Max Cost", placeHolder = "$0.00", valueChanged = {newValue -> maxCost = newValue})
            Destination(navController)

            FilledButton(
                labelVal = "Calculate Route",
                navController = navController,
                destination = "Map",
                function = {addRouteEntry(routeName, location, maxCost)}
            )

        }
    }
}

@Composable
fun Destination(navController: NavController) {
    var numDestinations by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(numDestinations) {
            var text by remember { mutableStateOf("") }
            TextField(
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = { Text("Enter Destination") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        Button(onClick = {
            numDestinations++
        }) {
            Text("Add Destination +")
        }

        Button(onClick = {
            navController.navigate("Map")
        }) {
            Text("Calculate Route")
        }
    }
}

@Preview
@Composable
fun DestinationInputScreenPreview() {
    DestinationInputScreen(rememberNavController())
}