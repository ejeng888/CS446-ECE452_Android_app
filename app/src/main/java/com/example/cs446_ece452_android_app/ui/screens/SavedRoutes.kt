package com.example.cs446_ece452_android_app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
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

@Composable
fun SavedRoutes(navController: NavController) {
    val searchQuery = remember { mutableStateOf("") }
    val routes = listOf("Route 1", "Route 2", "Route 3", "Route 4")
    val filteredRoutes = routes.filter {
        it.contains(searchQuery.value, ignoreCase = true)
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
                modifier = Modifier.size(width = 360.dp, height = 50.dp)
            )
            Spacer(modifier = Modifier.size(30.dp))
            /*
            Button(
                onClick = {},
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(DarkBlue),
                modifier = Modifier.size(width = 360.dp, height = 160.dp)
            ) {
                Text(text = "Route 2")
            }
             */
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(filteredRoutes) { route ->
                    Button(
                        onClick = {
                            // Handle route selection
                            navController.navigate("Map")
                        },
                        colors = ButtonDefaults.buttonColors(DarkBlue),
                        shape = RectangleShape,
                        modifier = Modifier.size(width = 360.dp, height = 160.dp)// Adjust height as needed
                    ) {
                        Text(text = route)
                    }
                }
            }
        }
    }
}
