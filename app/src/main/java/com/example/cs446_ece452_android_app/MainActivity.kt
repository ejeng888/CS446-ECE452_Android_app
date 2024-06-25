package com.example.cs446_ece452_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.theme.CS446ECE452_Android_appTheme
import com.example.cs446_ece452_android_app.ui.screens.LoginScreen
import com.example.cs446_ece452_android_app.ui.screens.SignupScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                    composable(route = "Signup") {
                        SignupScreen(
                            navController
                        )
                    }
                    composable(route = "SavedRoutes") { // Add New Screens here
                        Screens()
                    }
                }
            }
        }
    }
}
