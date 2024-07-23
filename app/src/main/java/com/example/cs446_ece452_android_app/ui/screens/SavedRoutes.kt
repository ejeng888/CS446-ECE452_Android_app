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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    val creatorEmail : String,
    val endDest : String
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
                    val creatorEmail = document.getString("creatorEmail") ?: ""
                    val endDest = document.getString("endDest.destination") ?: ""

                    routes.add(RouteInformation(documentId, routeName, lastModifiedDate, creatorEmail, endDest))
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
                    val documentId = document.id
                    val routeName = document.getString("routeName") ?: ""
                    val lastModifiedDate = document.getString("lastModifiedDate") ?: ""
                    val creatorEmail = document.getString("creatorEmail") ?: ""
                    val endDest = document.getString("endDest.destination") ?: ""

                    routes.add(RouteInformation(documentId, routeName, lastModifiedDate, creatorEmail, endDest))
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
                .padding(paddingValues)
                .padding(bottom = 16.dp)
        ) {
            Spacer(modifier = Modifier.size(30.dp))
            Logo()
            Spacer(modifier = Modifier.height(20.dp))

            SearchBar(searchQuery, valueChanged = { newValue -> searchQuery = newValue })

            Spacer(modifier = Modifier.size(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(filteredRoutes) { route ->
                    HomePageEntry(
                        route = route,
                        function = {
                        rc.getRoute(route.documentID)
                        navController.navigate("Map")},
                        deleteRoute = { routes.removeIf { it.documentID == route.documentID } }
                    )
                }
            }
        }
    }
}