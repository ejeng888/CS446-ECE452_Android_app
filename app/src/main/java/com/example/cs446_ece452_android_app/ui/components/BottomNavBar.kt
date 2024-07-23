package com.example.cs446_ece452_android_app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.ui.theme.Blue5
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.example.cs446_ece452_android_app.ui.theme.Gray

data class BottomNavigationModel(
    val name: String,
    val route: String,
    val iconSelected: ImageVector,
    val iconDeseleted: ImageVector,
    val notifications: Int = 0
)

@Composable
fun BottomNavigationBar(navController: NavController) {

    val backStackEntry = navController.currentBackStackEntryAsState()
    val NavList = listOf(
        BottomNavigationModel("All Routes", "routes", Icons.Rounded.Home, Icons.Outlined.Home),
        BottomNavigationModel("Add a Route", "DestinationInput", Icons.Default.Add, Icons.Outlined.Add),
        BottomNavigationModel("Profile", "Profile", Icons.Default.Person, Icons.Outlined.Person)
    )

    Column {

        HorizontalDivider(
            color = Gray,
            modifier = Modifier
                .height(1.5.dp)
                .fillMaxWidth()
        )

        Row(
            modifier = Modifier
                .background(Blue1)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {

            NavList.forEach { item ->

                val selected = item.route == backStackEntry.value?.destination?.route
                val icon = if (selected) item.iconSelected else item.iconDeseleted
                val displayColor = if (selected) DarkBlue else Blue5

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 5.dp) // Modify this to make the clicked button circular
                        .clickable { navController.navigate(item.route) }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = item.name,
                        tint = displayColor,
                        modifier = Modifier.size(38.dp)
                    )
                    Text(
                        text = item.name,
                        fontSize = 9.sp,
                        textAlign = TextAlign.Center,
                        color = displayColor
                    )
                }

            }
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(rememberNavController())
}
