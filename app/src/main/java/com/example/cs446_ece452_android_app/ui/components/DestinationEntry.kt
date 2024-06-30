package com.example.cs446_ece452_android_app.ui.components

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
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue3
import com.example.cs446_ece452_android_app.ui.theme.Blue4
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun DestinationEntry(timeChanged: (String) -> Unit, destinationChanged: (String) -> Unit) {
    var destination by remember {
        mutableStateOf("")
    }
    var timeSpent by remember {
        mutableStateOf("02:00")
    }
    Row(
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Column {
            VerticalLine()
            Row(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(14.dp))
                    .background(Blue2)
                    .padding(2.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Icon(imageVector = Icons.Default.Schedule, contentDescription = "clock", tint= DarkBlue)
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
            VerticalLine()

        }

        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = destination,
            onValueChange = {
                destination = it
                destinationChanged(destination)
            },
            textStyle = TextStyle(color = DarkBlue, fontSize = 20.sp),
            singleLine = true,
            placeholder = {
                Text(text = "Add Stop", color = Blue4)
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Blue3,
                unfocusedContainerColor = Blue1
            ),
            trailingIcon = {
                Icon(
                    Icons.Default.Clear,
                    contentDescription = "clear text",
                    modifier = Modifier
                        .clickable {
                            destination = ""
                        }
                )
            }
        )
    }
}

@Composable
fun VerticalLine() {
    Row {
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .height(15.dp)
                .width(3.dp)
                .clip(RectangleShape)
                .background(Blue2)
        )
    }

}

@Preview
@Composable
fun DestinationEntryPreview() {
    DestinationEntry({}, {})
}