package com.example.cs446_ece452_android_app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.data.MapsApiClient
import com.example.cs446_ece452_android_app.data.RouteController
import com.example.cs446_ece452_android_app.ui.screens.DestinationInputScreen
import com.example.cs446_ece452_android_app.ui.screens.LoginScreen
import com.example.cs446_ece452_android_app.ui.screens.MapScreen
import com.example.cs446_ece452_android_app.ui.screens.ProfileScreen
import com.example.cs446_ece452_android_app.ui.screens.ResetPassword
import com.example.cs446_ece452_android_app.ui.screens.SavedRoutes
import com.example.cs446_ece452_android_app.ui.screens.SignupScreen
import com.example.cs446_ece452_android_app.ui.screens.TransitScreen
import com.example.cs446_ece452_android_app.ui.theme.CS446ECE452_Android_appTheme
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiKey = getString(R.string.google_maps_key)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, apiKey)
        }

        val placesClient = Places.createClient(this)
        val mapsClient = MapsApiClient(apiKey)
        val rc = RouteController(mapsClient)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            window.statusBarColor = getColor(R.color.black)

            CS446ECE452_Android_appTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "Login"
                ) {
                    composable(route = "Login") {
                        LoginScreen(navController)
                    }
                    composable(route = "Resetpassword") {
                        ResetPassword(navController)
                    }
                    composable(route = "Signup") {
                        SignupScreen(navController)
                    }
                    composable(route = "routes") {
                        SavedRoutes(navController = navController, rc = rc)
                    }
                    composable(route = "Profile") {
                        ProfileScreen(navController = navController)
                    }
                    composable(route = "DestinationInput") {
                        DestinationInputScreen(navController = navController, rc = rc, placesClient = placesClient)
                    }
                    composable(route = "Map") {
                        MapScreen(navController = navController, rc = rc)
                    }
                    composable(route = "Directions"){
                        TransitScreen(navController = navController, rc = rc)
                    }
                }
            }
        }
    }
}
