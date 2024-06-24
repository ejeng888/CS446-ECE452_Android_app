package com.example.cs446_ece452_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.theme.CS446ECE452_Android_appTheme
import com.example.cs446_ece452_android_app.ui.screens.LoginScreen
import com.example.cs446_ece452_android_app.ui.screens.SignupScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
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
                    composable(route = "NewScreen") { // Add New Screens here
                        Greeting(
                            name = "Martin"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CS446ECE452_Android_appTheme {
        Greeting("Android")
    }
}