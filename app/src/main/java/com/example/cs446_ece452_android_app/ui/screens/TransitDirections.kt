package com.example.cs446_ece452_android_app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.cs446_ece452_android_app.data.RouteController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.cs446_ece452_android_app.data.MapsApiClient
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.data.model.StepData


@Composable
fun TransitScreen(navController: NavController, rc: RouteController) {
    val stepDataList = mutableListOf<StepData>()
    rc.transitRouteInfo.forEach { route ->
        route.legs.forEach { leg -> //There should only be 1 leg per route
            leg.steps.forEach{ step ->
                if(step.mode == "TRANSIT"){

                    stepDataList.add(
                        StepData(
                            distance = step.distance,
                            instruction = step.navInstruction.instruction,
                            lat = step.end.latLng.lat,
                            lng = step.end.latLng.lng
                        )
                    )

                }
            }
        }
        //Reached a stop

    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Blue1)
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text(
                    text = "Start at ${rc.transitRouteInfo[0].legs[0].start.latLng.lat}, ${rc.transitRouteInfo[0].legs[0].start.latLng.lng}",
                    fontSize = 18.sp,
                    color = Color.Black
                )
            }
            stepDataList.forEach { stepData ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "${stepData.instruction}, for ${stepData.distance} meters until ${stepData.lat}, ${stepData.lng}",
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}

    /*rc.transitRouteInfo.forEach { route ->
        route.legs.forEach { leg ->
            //get leg.steps
            //get steps.mode and steps.navInstruction.instruction
            //Also get steps.distance (I think it's in meters)

            //Probably best to only include mode="TRANSIT"
            //Might also want to get steps.end.latLng.lat and .lng
            //Then reverse geolocate
        }
    }*/


/*
@Preview
@Composable
fun TransitDirectionsPreview() {
    TransitScreen(rememberNavController())
}*/
