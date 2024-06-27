package com.example.cs446_ece452_android_app.ui.screens

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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun DestinationInputScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Use the provided padding values
        ) {
            Destination(navController)
        }
    }
}

@Composable
fun Destination(navController: NavController) {
    var numDestinations by remember { mutableIntStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
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
            addAlanTuring()
            navController.navigate("Map")
        }) {
            Text("Calculate Route")
        }
    }
}

private fun addAlanTuring() {
    val db = Firebase.firestore
    // [START add_alan_turing]
    // Create a new user with a first, middle, and last name
    val user = hashMapOf(
        "first" to "Alan",
        "middle" to "Mathison",
        "last" to "Turing",
        "born" to 1912,
    )

    // Add a new document with a generated ID
    db.collection("users")
        .add(user)
    // [END add_alan_turing]
}