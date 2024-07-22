package com.example.cs446_ece452_android_app.ui.components


import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.example.cs446_ece452_android_app.ui.theme.Blue2
import com.example.cs446_ece452_android_app.ui.theme.Blue3
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OutlinedInputBox(labelVal: String, icon: ImageVector, valueChanged : (String) -> Unit = {}) {
    var textVal by remember {
        mutableStateOf("")
    }
    val typeOfKeyboard: KeyboardType = when (labelVal) {
        "email ID" -> KeyboardType.Email    
        "mobile" -> KeyboardType.Phone
        else -> KeyboardType.Text
    }
    OutlinedTextField(
        value = textVal,
        onValueChange = {
            textVal = it
            valueChanged(textVal)
        },
        placeholder = {
            Text(text = labelVal, color = DarkBlue)
        },
        textStyle = TextStyle(color = DarkBlue),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Blue3,
            unfocusedContainerColor = Blue2,
            unfocusedLeadingIconColor = DarkBlue,
            focusedLeadingIconColor = DarkBlue
        ),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "profile"
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = typeOfKeyboard,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}
@Preview
@Composable
fun OutlinedInputBoxPreview() {
    OutlinedInputBox(labelVal = "Test!", icon = Icons.Default.DateRange)
}