package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.White
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun FilledButton(labelVal: String, navController: NavController, destination : String = "") {
    Button(
        onClick = { navController.navigate(destination) },
        colors = ButtonDefaults.buttonColors(
            containerColor = DarkBlue
        ),
        modifier = Modifier
            .size(width = 300.dp, height = 40.dp)

    ) {
        Text(
            text = labelVal,
            color = White
        )
    }
}

@Preview
@Composable
fun FilledButtonPreview() {
        FilledButton(labelVal = "Click me please!", rememberNavController())
}

