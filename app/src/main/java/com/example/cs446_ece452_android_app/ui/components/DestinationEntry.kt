package com.example.cs446_ece452_android_app.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue3
import com.example.cs446_ece452_android_app.ui.theme.Blue4
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.delay

@Composable
fun DestinationEntry(placesClient: PlacesClient, timeChanged: (String) -> Unit, destinationChanged: (String) -> Unit, start: Boolean = false, end: Boolean = false) {
    var destination by remember { mutableStateOf("") }
    var timeSpent by remember { mutableStateOf("02:00") }
    var icon = Icons.Default.Schedule
    var placeholder = "Add Stop"

    if (start) {
        icon = Icons.Default.LocationOn
        placeholder = "Enter Starting Location*"
    } else if (end) {
        icon = Icons.Default.Flag
        placeholder = "Enter Ending Location*"
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp) // Ensure the Row takes the full width
    ) {
        Column {
            VerticalLine(dontShow = start)
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(Blue2)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Icon(imageVector = icon, contentDescription = "clock", tint = DarkBlue)
                if (!start && !end) {
                    BasicTextField(
                        value = timeSpent,
                        onValueChange = {
                            timeSpent = it
                            timeChanged(timeSpent)
                        },
                        modifier = Modifier.width(55.dp),
                        textStyle = TextStyle(color = DarkBlue, fontSize = 20.sp),
                        singleLine = true
                    )
                }
            }
            VerticalLine(dontShow = end)
        }

        Box(modifier = Modifier.fillMaxWidth()) {
            AutocompleteTextField(
                placesClient = placesClient,
                value = destination,
                onValueChange = {
                    destination = it
                    destinationChanged(destination)
                },
                placeholder = placeholder,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AutocompleteTextField(
    placesClient: PlacesClient,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    var hasFocus by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    var suggestions by remember { mutableStateOf(listOf<Place>()) }

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            delay(100)
            val request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build()
            placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                suggestions = response.autocompletePredictions.map {
                    Place.builder()
                        .setName(it.getPrimaryText(null).toString())
                        .build()
                }
            }.addOnFailureListener { exception ->
                Log.e("Autocomplete", "Error getting autocomplete predictions", exception)
            }
        } else {
            suggestions = emptyList()
        }
    }

    Box(
        modifier = modifier
            .navigationBarsWithImePadding() // Adjust for keyboard
            .padding(start = 16.dp)
    ) { // Adjust this value to set the left padding
        Column {
            if (hasFocus && suggestions.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .background(Color.White)
                        .padding(4.dp)
                        .fillMaxWidth()
                ) {
                    Column {
                        suggestions.forEach { place ->
                            Text(
                                text = place.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onValueChange(place.name)
                                        suggestions = emptyList()
                                    }
                                    .padding(8.dp),
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            TextField(
                value = value,
                modifier = Modifier.onFocusChanged { focusState -> hasFocus = focusState.isFocused },
                onValueChange = {
                    onValueChange(it)
                    query = it
                },
                placeholder = { Text(text = placeholder, color = Blue4) },
                textStyle = TextStyle(color = DarkBlue, fontSize = 20.sp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Blue3,
                    unfocusedContainerColor = Blue1
                ),
                trailingIcon = {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = Modifier.clickable {
                            onValueChange("")
                            suggestions = emptyList()
                        }
                    )
                }
            )
        }
    }
}


@Composable
fun VerticalLine(dontShow: Boolean = false) {
    Row {
        Spacer(
            modifier = Modifier
                .width(12.dp)
                .height(15.dp)
        )
        if (dontShow) return
        Box(
            modifier = Modifier
                .height(15.dp)
                .width(3.dp)
                .clip(RectangleShape)
                .background(Blue2)
        )
    }

}
