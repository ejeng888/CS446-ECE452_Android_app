package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun OutlinedButton(labelVal: String, navController: NavController, destination : String = "", function : () -> Unit = {}) {
    OutlinedButton(
        onClick = {
            function()
            if (destination != "") navController.navigate(destination)
                  },
        colors = ButtonDefaults.buttonColors(
            containerColor = Blue1,
            contentColor = DarkBlue
        ),
        modifier = Modifier
            .size(width = 300.dp, height = 40.dp)

    ) {
        Text(
            text = labelVal,
            color = DarkBlue
        )
    }
}

@Preview
@Composable
fun OutlinedButtonPreview() {
        OutlinedButton(labelVal = "Click me please!", rememberNavController())
}

