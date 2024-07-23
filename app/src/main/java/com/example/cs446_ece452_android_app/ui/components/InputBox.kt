package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Modifier
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.Blue3
import com.example.cs446_ece452_android_app.ui.theme.Blue4
import com.example.cs446_ece452_android_app.ui.theme.Blue5
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun InputBox(labelVal: String, placeHolder: String = "", value: String, valueChanged: (String) -> Unit = {}, enabled: Boolean = true) {
    var textVal by remember { mutableStateOf(value) }
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = {
            textVal = it
            valueChanged(textVal)
        },
        label = { Text(labelVal, color = Blue5) },
        enabled = enabled,
        singleLine = true,
        textStyle = TextStyle(color = DarkBlue, fontSize = 20.sp),
        placeholder = {
            Text(text = placeHolder, color = Blue4)
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Blue3,
            unfocusedContainerColor = Blue1
        )
    )
}

@Preview
@Composable
fun InputBoxPreview() {
    InputBox("Route Name", "NY Grad Trip", "", {})
}