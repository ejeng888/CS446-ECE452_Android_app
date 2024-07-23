package com.example.cs446_ece452_android_app.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Today
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cs446_ece452_android_app.R
import com.example.cs446_ece452_android_app.ui.screens.RouteInformation
import com.example.cs446_ece452_android_app.ui.theme.Blue1
import com.example.cs446_ece452_android_app.ui.theme.DarkBlue
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

@Composable
fun HomePageEntry(route : RouteInformation, function : () -> Unit = {}, deleteRoute: () -> Unit = {}, placesClient: PlacesClient) {

    val routeName = route.routeName
    val lastModifiedDate = route.lastModifiedDate
    val documentID = route.documentID
    val creatorEmail = route.creatorEmail

    val fields = listOf(Place.Field.PHOTO_METADATAS)
    val endDestID = route.endDestID
    val placeRequest = FetchPlaceRequest.newInstance(endDestID, fields)
    val context = LocalContext.current

    val initialBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.default_home_image)

    var bitmapState = remember { mutableStateOf<Bitmap>(initialBitmap) }

    LaunchedEffect(Unit) {
        placesClient.fetchPlace(placeRequest)
            .addOnSuccessListener { response: FetchPlaceResponse ->
                val place = response.place

                // Get the photo metadata.
                val metada = place.photoMetadatas
                if (metada == null || metada.isEmpty()) {
                    Log.w("HomePageEntry", "No photo metadata.")
                    return@addOnSuccessListener
                }
                val photoMetadata = metada.first()

                // Get the attribution text.
                val attributions = photoMetadata?.attributions

                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500) // Optional.
                    .setMaxHeight(300) // Optional.
                    .build()
                Log.v("HomePageEntry", "Before fetching Photo")
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        bitmapState.value = fetchPhotoResponse.bitmap
                        // ImageView.setImageBitmap(bitmap)
                        Log.v("HomePageEntry", "After fetching Photo")
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            Log.e("HomePageEntry", "Place not found: " + exception.message)
                        }
                    }
            }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 0.dp, end = 20.dp)
    ) {
        Button(
            onClick = {
                function()
            },
            colors = ButtonDefaults.buttonColors(Blue1),
            shape = RectangleShape,
            modifier = Modifier
                .height(100.dp)
                .weight(1f)
                .padding(end = 0.dp) // Adjust height as needed
        ) {

            Image(
                bitmap = bitmapState.value.asImageBitmap(),
                contentDescription = "Displayed Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(85.dp)
            )

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 5.dp) // Add padding inside the button
            ) {

                Text(
                    text = routeName,
                    color = DarkBlue,
                    fontSize = 20.sp, // Adjust text size as needed
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start

                ) {
                    Icon(
                        imageVector = Icons.Rounded.Today,
                        contentDescription = "Calendar",
                        tint = DarkBlue,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(12.dp)
                    )
                    Text(
                        text = lastModifiedDate,
                        color = DarkBlue,
                        fontSize = 12.sp
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = "Calendar",
                        tint = DarkBlue,
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .size(12.dp)
                    )
                    Text(
                        text = creatorEmail,
                        color = DarkBlue,
                        fontSize = 12.sp
                    )
                }

            }
        }
        ShareIconButton(documentID)

        DeleteIconButton(documentId = documentID, deleteRoute = {deleteRoute()})

    }

}

@Composable
fun ShareIconButton(documentId: String) {
    var showDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    val db = Firebase.firestore

    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.size(width = 30.dp, height = 30.dp) // Adjust height as needed
    ) {
        Icon(
            painter = painterResource(id = R.drawable.share_icon), // replace with your share icon
            contentDescription = "Share",
            tint = Color.Gray
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Share via Email") },
            text = {
                Column {
                    Text(text = "Enter email address:")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text(text = "Email") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Firestore update logic
                        val docRef = db.collection("routeEntries").document(documentId)

                        db.runTransaction { transaction ->
                            val snapshot = transaction.get(docRef)
                            val currentEmails = snapshot.get("sharedEmails") as? MutableList<String> ?: mutableListOf()
                            if (!currentEmails.contains(email)) {
                                currentEmails.add(email)
                            }
                            transaction.update(docRef, "sharedEmails", currentEmails)
                        }.addOnSuccessListener {
                            Log.e("TRAN", "Transaction successfully committed!")
                        }.addOnFailureListener { e ->
                            Log.e("TRAN", "Transaction failed: $e")
                        }

                        showDialog = false
                    }
                ) {
                    Text("Share")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DeleteIconButton(documentId: String, deleteRoute : () -> Unit = {}) {
    val db = Firebase.firestore
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(
        onClick = { showDialog = true },
        modifier = Modifier.size(width = 40.dp, height = 40.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Delete,
            contentDescription = "Delete",
            tint = Color.Red,
            modifier = Modifier.size(40.dp)
        )
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = {
                Text(text = "Delete Route")
            },
            text = {
                Text("Are you sure you want to delete Route?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        db.collection("routeEntries").document(documentId)
                            .delete()
                            .addOnSuccessListener {
                                showDialog = false
                                deleteRoute()
                                toastHelper(context, "Deleted Route")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error deleting document", e)
                                // Optionally handle the failure case
                            }
                    },
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }
}