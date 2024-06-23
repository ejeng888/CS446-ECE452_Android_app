package com.example.cs446_ece452_android_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun Screens(){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "first_Screen") {
        composable("first_Screen"){
            SavedRoutes(navController = navController)
        }
    }
}

@Composable
fun SavedRoutes(navController: NavController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .background(Blue2)
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
            onClick = {},
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
        Spacer(modifier = Modifier.size(240.dp))
        Divider(color = Color.Gray, thickness = 1.dp)
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()
        ){
            IconButton(
                onClick = {},
                modifier = Modifier.size(100.dp).padding(start = 30.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "All Routes",
                    tint = DarkBlue
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.size(100.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Route",
                    tint = DarkBlue
                )
            }
            IconButton(
                onClick = {},
                modifier = Modifier.size(100.dp).padding(end = 30.dp)
            ) {
                Icon(imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = DarkBlue
                )
            }
        }
    }

}