package com.example.cs446_ece452_android_app.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun SignupScreen(name: String, modifier: Modifier = Modifier, navController: NavHostController) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}


