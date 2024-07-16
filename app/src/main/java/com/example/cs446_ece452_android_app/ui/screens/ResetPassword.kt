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
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.components.toastHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

@Composable
fun ResetPassword(navController : NavController) {
    val context = LocalContext.current

    var enteredEmail by remember { mutableStateOf("") }

    fun reset(context: Context, email: String, destination: String) {
        var toastMessage = ""
        val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
        if (!emailRegex.toRegex().matches(email)) {
            // Not a valid email
            toastMessage = "Please Enter a Valid Email"
            toastHelper(context, toastMessage)
        }

        if (toastMessage == "") {
            Log.v("EmailPassword", "Passes Frontend Checks")
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        toastMessage = "Check Inbox for Password Reset"
                        toastHelper(context, toastMessage)
                        Log.d("ResetPassword", "Email sent.")
                        navController.navigate(destination)
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


            Spacer(modifier = Modifier.height(30.dp))
            FilledButton(labelVal = "Reset Password", navController = navController, function = {
                reset(context = context, email = enteredEmail, destination = "Login") // New screen
            })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(labelVal = "Go Back", navController = navController, destination = "Login")


        }
    }
}

@Preview
@Composable
fun ResetPasswordPreview() {
    ResetPassword(rememberNavController())
}

