package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.screens.RouteInformation
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun HomePageEntry(route : RouteInformation, function : () -> Unit = {}) {
    val routeName = route.routeName
    val lastModifiedDate = route.lastModifiedDate
    // val documentId = route.documentID
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 10.dp, end = 20.dp)
    ) {
        Button(
            onClick = {
                function()
            },
            colors = ButtonDefaults.buttonColors(Blue1),
            shape = RectangleShape,
            modifier = Modifier
                .size(width = 360.dp, height = 120.dp)
                .padding(end = 10.dp)// Adjust height as needed
        ) {
            Text(
                text = "[Insert Map]",
                color = DarkBlue,
                fontSize = 16.sp, // Adjust text size as needed
                fontWeight = FontWeight.Bold
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp) // Add padding inside the button
            ) {
                Text(
                    text = routeName,
                    color = DarkBlue,
                    fontSize = 20.sp, // Adjust text size as needed
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp)) // Space between texts
                Text(
                    text = lastModifiedDate,
                    color = DarkBlue,
                    fontSize = 14.sp // Adjust text size as needed
                )
            }
        }
        IconButton(
            onClick = {
//                                routeToDelete = documentId
//                                routeNameDelete = "$routeName - $date - $documentId"
//                                routeNameId = routeName
//                                showDialog = true
            },
            modifier = Modifier.size(width = 40.dp, height = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = "Delete",
                tint = Color.Red,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

@Preview
@Composable
fun HomePageEntryPreview() {
    HomePageEntry(RouteInformation("documentId", "RouteName", "lastModifiedDate"))
}