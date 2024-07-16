package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.R
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue

@Composable
fun Logo() {
    val fugazoneFamily = FontFamily(
        Font(R.font.fugazone_regular, FontWeight.Normal)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon( modifier = Modifier.size(60.dp), imageVector = Icons.Rounded.LocationOn, contentDescription = "Password Requirement", tint = DarkBlue, )
        Text(
            text = "TrailBlazer",
            modifier = Modifier.padding(2.dp),
            color = DarkBlue,
            fontSize = 45.sp,
            fontFamily = fugazoneFamily,
            fontWeight = FontWeight.Normal
        )
    }
}

@Preview
@Composable
fun LogoPreview() {
    Logo()
}

