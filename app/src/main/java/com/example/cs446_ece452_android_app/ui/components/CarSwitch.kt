package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue5
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun CarSwitch(Switched: (Boolean) -> Unit) {
    var accessToCar by remember {
        mutableStateOf(false)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(0.dp)
    ) {
        Text(
            text = "Access to Car",
            color = Blue5,
            fontSize = 12.sp
        )
        Switch(
            modifier = Modifier.fillMaxWidth(),
            checked = accessToCar,
            onCheckedChange = {
                accessToCar = it
                Switched(accessToCar)
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = DarkBlue,
                uncheckedTrackColor = Blue2
            ),
            thumbContent = if (accessToCar) {
                {
                    Icon(
                        imageVector = Icons.Filled.DirectionsCar,
                        contentDescription = null,
                        modifier = Modifier.size(SwitchDefaults.IconSize),
                    )
                }
            } else {
                null
            }
        )
    }
}

@Preview
@Composable
fun CarSwitchPreview() {
    CarSwitch({})
}