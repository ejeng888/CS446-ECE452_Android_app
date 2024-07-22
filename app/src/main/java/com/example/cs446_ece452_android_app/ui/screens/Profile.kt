package com.example.cs446_ece452_android_app.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.components.toastHelper
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest

@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue1)
                .padding(paddingValues)
                .padding(30.dp)
        ) {
            var givenDisplayName by remember { mutableStateOf("") }
            // var givenProfilePicture by remember { mutableStateOf("") }
            val user = Firebase.auth.currentUser
            val context = LocalContext.current
            user?.let {
                // val photoUrl = it.photoUrl

                Logo()

                InputBox(labelVal = "Display Name", placeHolder = "", givenDisplayName, valueChanged = { newValue -> givenDisplayName = newValue })
                InputBox(labelVal = "Email", placeHolder = "", (it.email ?: ""), enabled = false)


                FilledButton(labelVal = "Update Personal Information", navController = navController, function = {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = givenDisplayName
                        // photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
                    }

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                toastHelper(context = context, toastMessage = "Successfully Updated Information")
                                Log.d("Profile", "User profile updated.")
                                navController.navigate("routes")
                            }
                        }
                })

                FilledButton(labelVal = "Log Out", navController = navController, function = {
                    Firebase.auth.signOut()
                    toastHelper(context = context, toastMessage = "Successfully Logged Out")
                    Log.d("Profile", "Logged Out.")
                    navController.navigate("Login")
                })

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedButton(labelVal = "Delete Account", navController = navController, function = {
                    val deleteUser = Firebase.auth.currentUser!!
                    deleteUser.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                toastHelper(context = context, toastMessage = "User Account Deleted")
                                Log.d("Profile", "User account deleted.")
                                navController.navigate("Login")
                            }
                        }
                })
            }
        }
    }
}

@Preview
@Composable
fun ProfilePreview() {
    ProfileScreen(rememberNavController())
}
