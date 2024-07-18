package com.example.cs446_ece452_android_app.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.R
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SavedRoutes(navController: NavController) {
    val searchQuery = remember { mutableStateOf("") }
    var routes by remember { mutableStateOf(listOf<String>()) }
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid
    LaunchedEffect(Unit) {
        db.collection("routeEntries")
            //.whereEqualTo("userId", currentUserId)
            .get()
            .addOnSuccessListener { documents ->
                val routesList = mutableListOf<String>()
                for (document in documents) {
                    val routeName = document.getString("routeName") ?: ""
                    val date = document.getString("startDate") ?: ""
                    val documentId = document.id
                    routesList.add("$routeName - $date - $documentId")
                }
                // Update the routes state with fetched routes
                routes = routesList
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }
    val filteredRoutes = routes.filter {
        it.contains(searchQuery.value, ignoreCase = true)
    }
    var routeToDelete by remember { mutableStateOf<String?>(null) }
    var routeNameDelete by remember { mutableStateOf<String?>(null) }
    var routeNameId by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }

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
                .background(Blue2)
                .padding(paddingValues) // Use the provided padding values
                .padding(bottom = 16.dp) // Additional padding if needed
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Logo()
            Spacer(modifier = Modifier.height(30.dp))
            TextField(
                value = searchQuery.value,
                onValueChange = { newValue -> searchQuery.value = newValue },
                placeholder = { Text("Search...") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search), // replace with your search icon
                        contentDescription = "Search"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = DarkBlue
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )
            Spacer(modifier = Modifier.size(30.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(filteredRoutes) { route ->
                    val parts = route.split(" - ")
                    val routeName = parts[0]
                    val date = parts[1]
                    val documentId = parts[2]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 10.dp, end = 20.dp)
                    ) {
                        Button(
                            onClick = {
                                // Handle route selection
                                navController.navigate("Map")
                            },
                            colors = ButtonDefaults.buttonColors(Blue2),
                            shape = RectangleShape,
                            modifier = Modifier
                                .size(width = 360.dp, height = 120.dp)
                                .padding(end = 10.dp)// Adjust height as needed
                        ) {
                            Text(
                                text = "Map",
                                color = DarkBlue,
                                fontSize = 16.sp, // Adjust text size as needed
                                fontWeight = FontWeight.Bold
                            )
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp) // Add padding inside the button
                            ) {
                                Text(
                                    text = routeName,
                                    color = DarkBlue,
                                    fontSize = 16.sp, // Adjust text size as needed
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp)) // Space between texts
                                Text(
                                    text = date,
                                    color = DarkBlue,
                                    fontSize = 14.sp // Adjust text size as needed
                                )
                            }
                        }
                        IconButton(
                            onClick = {
                                routeToDelete = documentId
                                routeNameDelete = "$routeName - $date - $documentId"
                                routeNameId = routeName
                                showDialog = true
                            },
                            modifier = Modifier.size(width = 30.dp, height = 30.dp)// Adjust height as needed
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_icon), // replace with your search icon
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        routeToDelete = null
                        routeNameDelete = null
                        routeNameId = null
                        showDialog = false
                    },
                    title = {
                        Text(text = "Delete Route")
                    },
                    text = {
                        // routeNameId is a required field
                        Text("Are you sure you want to delete $routeNameId?")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                db.collection("routeEntries").document(routeToDelete!!)
                                    .delete()
                                    .addOnSuccessListener {
                                        // Delete from local state
                                        routes = routes.filter { it != routeNameDelete }
                                        routeToDelete = null
                                        routeNameDelete = null
                                        routeNameId = null
                                        showDialog = false
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Error deleting document", e)
                                        // Optionally handle the failure case
                                    }
                            },
                        ) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        Button(
                            onClick = {
                                routeToDelete = null
                                routeNameDelete = null
                                routeNameId = null
                                showDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }
    }
}


@Preview
@Composable
fun SavedRoutesPreview() {
    SavedRoutes(rememberNavController())
}