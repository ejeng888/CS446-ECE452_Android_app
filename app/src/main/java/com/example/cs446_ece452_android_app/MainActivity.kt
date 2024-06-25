package com.example.cs446_ece452_android_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.theme.CS446ECE452_Android_appTheme
import com.example.cs446_ece452_android_app.ui.screens.LoginScreen
import com.example.cs446_ece452_android_app.ui.screens.SignupScreen
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            window.statusBarColor = getColor(R.color.black)
            Screens()
            /*
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
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                            name = "AndroidTEST",
                            modifier = Modifier.padding(innerPadding)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    CustomButton(onClick = {
                        // Handle button click action here
                    })*/
                }
            }*/

        }
    }
}

@Composable
fun CustomButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(text = "Login")
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