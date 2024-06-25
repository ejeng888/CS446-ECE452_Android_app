package com.example.cs446_ece452_android_app

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun BottomNavigationBar(navController: NavController) {
    val context = LocalContext.current
    val startGoogleMapsActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle result if needed
    }

    BottomAppBar {
        IconButton(
            onClick = { navController.navigate("routes") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = "All Routes",
                tint = DarkBlue
            )
        }
        IconButton(
            onClick = {
                navController.navigate("maps")
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Route",
                tint = DarkBlue
            )
        }
        IconButton(
            onClick = { navController.navigate("Profile") },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = DarkBlue
            )
        }
    }
}
