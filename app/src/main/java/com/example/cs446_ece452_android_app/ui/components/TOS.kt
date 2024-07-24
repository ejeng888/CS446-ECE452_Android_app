package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.Gray

@Composable
fun TOS(checkedChanged: (Boolean) -> Unit = {}) {
    var checked by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
            .padding(top = 10.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                checked = it
                checkedChanged(checked)
            },
            colors = CheckboxDefaults.colors(
                uncheckedColor = Gray,
                checkedColor = DarkBlue)
        )
        Text(
            text = "Agree to ",
            color = DarkBlue,
            fontSize = 14.sp
        )
        Text(
            text = "Google Map's Terms of Service",
            style = TextStyle(textDecoration = TextDecoration.Underline),
            color = DarkBlue,
            fontSize = 14.sp,
            modifier = Modifier.clickable{
                uriHandler.openUri("https://cloud.google.com/maps-platform/terms")
            }
        )
    }
}