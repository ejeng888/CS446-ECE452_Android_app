package com.example.cs446_ece452_android_app.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.material3.Surface
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Spacer
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.components.OutlinedInputBox
import com.example.cs446_ece452_android_app.ui.components.PasswordInputBox
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.Gray
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun SignupScreen(navController : NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val context = LocalContext.current

    var enteredEmail by remember { mutableStateOf("") }
    var enteredPassword by remember { mutableStateOf("") }
    var enteredPassword2 by remember { mutableStateOf("") }


    fun createAccount(context: Context, email: String, password1: String, password2: String, destination: String) {
        var toastMessage = "Successfully Made Account"
        val emailRegex = "^[A-Za-z](.*)(@)(.+)(\\.)(.+)"
        if (!emailRegex.toRegex().matches(email)) {
            // Not a valid email
            toastMessage = "Please Enter a Valid Email"
        } else if (password1.length < 8) {
            // Password length too short
            toastMessage = "Password Length is Too Short"
        } else if (password1.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            toastMessage = "No Uppercase Characters"
        } else if (password1.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) {
            toastMessage = "No Lowercase Characters"
        } else if (password1.firstOrNull { it.isDigit() } == null) {
            toastMessage = "No Digits"
        } else if (password1.firstOrNull { !it.isLetterOrDigit() } == null) {
            toastMessage = "No Special Characters"
        }
        else if (password1 != password2) {
            toastMessage = "Passwords are not the same"
        }

        if (toastMessage == "Successfully Made Account") {
            Log.v("EmailPassword", "Passes Frontend Checks")
            auth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("EmailPassword", "createUserWithEmail:success")
                        navController.navigate(destination)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("EmailPassword", "createUserWithEmail:failure", task.exception)
                        toastMessage = "Unable to Create Account, Firebase Issue"
                    }
                }
        }

        // Final Message Displayed
        Toast.makeText(
            context,
            toastMessage,
            Toast.LENGTH_SHORT,
        ).show()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Blue1
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(20.dp))
            Logo()
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedInputBox(labelVal = "Email", Icons.Default.Email, valueChanged = {newValue -> enteredEmail = newValue})
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInputBox(labelVal = "Password", valueChanged = {newValue -> enteredPassword = newValue})
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInputBox(labelVal = "Reenter Password", valueChanged = {newValue -> enteredPassword2 = newValue})

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                PasswordRequirement(text = "Minimum 8 characters", fulfilled = (enteredPassword.length >= 8))
                PasswordRequirement(text = "1 Uppercase Letter", fulfilled = (enteredPassword.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } != null))
                PasswordRequirement(text = "1 Lowercase Letter", fulfilled = (enteredPassword.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } != null))
                PasswordRequirement(text = "1 Numerical", fulfilled = (enteredPassword.firstOrNull { it.isDigit() } != null))
                PasswordRequirement(text = "1 Special Character", fulfilled = (enteredPassword.firstOrNull { !it.isLetterOrDigit() } != null))
            }


            Spacer(modifier = Modifier.height(30.dp))
            FilledButton(labelVal = "Signup", navController = navController, function = {
                createAccount(context = context, email = enteredEmail, password1 = enteredPassword, password2= enteredPassword2, destination = "routes") // New screen
            })
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(labelVal = "Back to Login", navController = navController, destination = "Login")

        }
    }
}

@Composable
fun PasswordRequirement(text: String, fulfilled: Boolean) {
    val icon = if (fulfilled) Icons.Default.Check else Icons.Default.Close
    val tint = if (fulfilled) Gray else DarkBlue
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp)
    ) {
        Icon(imageVector = icon, contentDescription = "Password Requirement", tint = tint)
        Text(
            text = text,
            modifier = Modifier.padding(2.dp),
            color = tint,
            fontSize = 12.sp
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SignupScreenPreview() {
    SignupScreen(rememberNavController())
}

