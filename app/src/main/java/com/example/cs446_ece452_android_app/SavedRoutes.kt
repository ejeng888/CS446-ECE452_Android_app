package com.example.cs446_ece452_android_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun SavedRoutes(navController: NavController) {
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
            Text(
                text = "Trailblazer",
                color = DarkBlue,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 50.sp
            )
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(DarkBlue),
                shape = RectangleShape,
                modifier = Modifier.size(width = 360.dp, height = 50.dp)
            ) {
                Text(text = "Search Bar")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {
                    navController.navigate("Map")
                },
                colors = ButtonDefaults.buttonColors(DarkBlue),
                shape = RectangleShape,
                modifier = Modifier.size(width = 360.dp, height = 160.dp)
            ) {
                Text(text = "Route 1")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(
                onClick = {},
                shape = RectangleShape,
                colors = ButtonDefaults.buttonColors(DarkBlue),
                modifier = Modifier.size(width = 360.dp, height = 160.dp)
            ) {
                Text(text = "Route 2")
            }
        }
    }
}
