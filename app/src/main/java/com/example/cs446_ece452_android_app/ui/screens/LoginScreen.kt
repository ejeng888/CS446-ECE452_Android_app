package com.example.cs446_ece452_android_app.ui.screens

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import com.example.cs446_ece452_android_app.ui.components.OutlinedInputBox
import com.example.cs446_ece452_android_app.ui.components.PasswordInputBox
import com.example.cs446_ece452_android_app.ui.components.TextDivider

@Composable
fun LoginScreen() {
    var password by remember { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Blue1
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Image(
                painter = painterResource(id = R.drawable.login_picture),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .size(250.dp)

            )
            Spacer(modifier = Modifier.height(30.dp))
            Logo()
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedInputBox(labelVal = "Email", Icons.Default.Email)
            Spacer(modifier = Modifier.height(10.dp))
            PasswordInputBox()

            TextDivider(text="or")






        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}

