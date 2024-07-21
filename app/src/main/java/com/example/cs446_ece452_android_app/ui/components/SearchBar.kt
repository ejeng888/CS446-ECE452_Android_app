package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue3
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.Gray

@Composable
fun SearchBar(searchQuery: MutableState<String>, valueChanged: (MutableState<String>) -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,

    ) {
        OutlinedTextField(
            value = searchQuery.value,
            onValueChange = {
                    newValue -> searchQuery.value = newValue
                valueChanged(searchQuery)
            },
            placeholder = { Text("Search...", color = Gray) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Blue3,
                unfocusedContainerColor = Blue2,
                unfocusedLeadingIconColor = DarkBlue,
                focusedLeadingIconColor = DarkBlue
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Bar"
                )
            },
            singleLine = true,

        )

        Icon(
            imageVector = Icons.Rounded.Tune,
            contentDescription = "Search Bar",
            modifier = Modifier.size(25.dp)
        )
    }

}

@Preview
@Composable
fun SearchBarPreview() {
    val searchQuery = remember { mutableStateOf("") }
    SearchBar(searchQuery =  searchQuery)
}