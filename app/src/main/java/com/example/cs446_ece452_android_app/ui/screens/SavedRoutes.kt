package com.example.cs446_ece452_android_app.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.data.RouteController
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.auth.FirebaseAuth
import com.example.cs446_ece452_android_app.ui.components.HomePageEntry
import com.example.cs446_ece452_android_app.ui.components.SearchBar
import com.example.cs446_ece452_android_app.ui.theme.Blue1

data class RouteInformation(
    val documentID : String,
    val routeName : String,
    val lastModifiedDate : String,

)


@Composable
fun SavedRoutes(navController: NavController, rc: RouteController) {

    var searchQuery = remember { mutableStateOf("") }
    val routes = remember { mutableStateListOf<RouteInformation>() }
    val db = Firebase.firestore
    val auth = FirebaseAuth.getInstance()
    val currUser = auth.currentUser?.email
    LaunchedEffect(Unit) {
        db.collection("routeEntries")
            .whereEqualTo("creatorEmail", currUser)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id
                    val routeName = document.getString("routeName") ?: ""
                    val lastModifiedDate = document.getString("lastModifiedDate") ?: ""

                    routes.add(RouteInformation(documentId, routeName, lastModifiedDate))
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("Firestore", "Error getting documents: ", exception)
            }
        db.collection("routeEntries")
            .whereNotEqualTo("creatorEmail", currUser)
            .whereArrayContains("sharedEmails", currUser ?: "")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val routeName = document.getString("routeName") ?: ""
                    val lastModifiedDate = document.getString("lastModifiedDate") ?: ""
                    val documentId = document.id
                    routes.add(RouteInformation(documentId, routeName, lastModifiedDate))
                }
            }
            .addOnFailureListener { exception ->
                // Handle errors
                Log.e("Firestore", "Error getting documents: ", exception)
            }
    }

    val filteredRoutes = routes.filter {
        it.routeName.contains(searchQuery.value, ignoreCase = true)
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
                .background(Blue1)
                .padding(paddingValues) // Use the provided padding values
                .padding(bottom = 16.dp) // Additional padding if needed
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Logo()
            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(searchQuery, valueChanged = { newValue -> searchQuery = newValue })

            Spacer(modifier = Modifier.size(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(filteredRoutes) { route ->
                    HomePageEntry(route = route, function = {
                        rc.getRoute(route.documentID)
                        navController.navigate("Map")}
                    )
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
                                        // routes = routes.filter { it != routeNameDelete }
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
