package com.example.cs446_ece452_android_app.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import com.example.cs446_ece452_android_app.R
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.ui.components.OutlinedInputBox
import com.example.cs446_ece452_android_app.ui.components.PasswordInputBox
import com.example.cs446_ece452_android_app.ui.components.TextDivider
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.components.toastHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun LoginScreen(navController : NavController) {
    val auth: FirebaseAuth = Firebase.auth
    val context = LocalContext.current

    var enteredEmail by remember { mutableStateOf("") }
    var enteredPassword by remember { mutableStateOf("") }

    fun login(context: Context, email: String, password: String, destination: String) {
        // Default Toast Message, will only change if Login works
        var toastMessage = "Username or Password is Incorrect"
        val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
        if (!emailRegex.toRegex().matches(email)) {
            // Not a valid email
            toastMessage = "Please Enter a Valid Email"
            toastHelper(context, toastMessage)
        } else if (password == "") {
            toastMessage = "Please Enter a Password"
            toastHelper(context, toastMessage)
        }

        if (toastMessage == "Username or Password is Incorrect") {
            Log.v("EmailPassword", "Passes Frontend Checks")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        toastMessage = "Successfully Logged In"
                        toastHelper(context, toastMessage)
                        Log.d("EmailPassword", "createUserWithEmail:success")
                        navController.navigate(destination)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("EmailPassword", "createUserWithEmail:failure", task.exception)
                        toastMessage = "Username or Password is Incorrect"
                        toastHelper(context, toastMessage)
                    }
                }
        }

    }
        Surface(
        modifier = Modifier.fillMaxSize(),
        color = Blue1
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.login_picture),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)

            )
            Spacer(modifier = Modifier.height(20.dp))
            Logo()
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedInputBox(labelVal = "Email", Icons.Default.Email, valueChanged = {newValue -> enteredEmail = newValue})
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInputBox(labelVal = "Password", valueChanged = {newValue -> enteredPassword = newValue})

            Spacer(modifier = Modifier.height(30.dp))
            FilledButton(labelVal = "Login", navController = navController, function = {
                login(context = context, email = enteredEmail, password = enteredPassword, destination = "routes") // New screen
            })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(labelVal = "Create Account", navController = navController, destination = "Signup")

            TextDivider(text="or")


        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(rememberNavController())
}

