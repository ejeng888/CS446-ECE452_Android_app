package com.example.cs446_ece452_android_app.ui.screens
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.bumptech.glide.Glide
import com.example.cs446_ece452_android_app.ui.components.BottomNavigationBar
import com.example.cs446_ece452_android_app.ui.components.FilledButton
import com.example.cs446_ece452_android_app.ui.components.InputBox
import com.example.cs446_ece452_android_app.ui.components.Logo
import com.example.cs446_ece452_android_app.ui.components.OutlinedButton
import com.example.cs446_ece452_android_app.ui.components.toastHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

@Composable
fun ProfileScreen(
    navController: NavController,
    pickMedia: ActivityResultLauncher<PickVisualMediaRequest>,
    imageUriState: MutableState<Uri?>
) {
    val context = LocalContext.current
    val user = Firebase.auth.currentUser
    val userId = user?.uid ?: return
    var profileImageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(userId) {
        loadProfileImage(userId) { imageUrl ->
            profileImageUrl = imageUrl
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(30.dp)
        ) {
            var givenDisplayName by remember { mutableStateOf("") }

            // Logo component
            Logo()

            // Profile Image
            AndroidView(
                factory = {
                    ImageView(context).apply {
                        layoutParams = LinearLayout.LayoutParams(
                            400,
                            400
                        ).apply {
                            // Optionally set margins
                            topMargin = 16
                        }
                        setBackgroundColor(0xFFE0E0E0.toInt()) // Set background color
                        scaleType = ImageView.ScaleType.CENTER_CROP
                        contentDescription = "Image View"

                        setOnClickListener {
                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    }
                },
                update = { imageView ->
                    imageUriState.value?.let { uri ->
                        // Use Glide to load the image from URI
                        Glide.with(context).load(uri).into(imageView)
                        uploadImageToFirebase(uri, userId)
                    } ?: run {
                        profileImageUrl?.let { url ->
                            // Use Glide to load the image from URL
                            Glide.with(context).load(url).into(imageView)
                        }
                    }
                }
            )

            user.let {
                InputBox(labelVal = "Display Name", placeHolder = "", givenDisplayName, valueChanged = { newValue -> givenDisplayName = newValue })
                InputBox(labelVal = "Email", placeHolder = "", (it.email ?: ""), enabled = false)

                FilledButton(labelVal = "Update Personal Information", navController = navController, function = {
                    val profileUpdates = userProfileChangeRequest {
                        displayName = givenDisplayName
                    }

                    user.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                toastHelper(context = context, toastMessage = "Successfully Updated Information")
                                Log.d("Profile", "User profile updated.")
                                navController.navigate("routes")
                            }
                        }
                })

                FilledButton(labelVal = "Log Out", navController = navController, function = {
                    Firebase.auth.signOut()
                    toastHelper(context = context, toastMessage = "Successfully Logged Out")
                    Log.d("Profile", "Logged Out.")
                    navController.navigate("Login")
                })

                Spacer(modifier = Modifier.padding(10.dp))

                OutlinedButton(labelVal = "Delete Account", navController = navController, function = {
                    val deleteUser = Firebase.auth.currentUser!!
                    deleteUser.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                toastHelper(context = context, toastMessage = "User Account Deleted")
                                Log.d("Profile", "User account deleted.")
                                navController.navigate("Login")
                            }
                        }
                })
            }
        }
    }
}

class CircularImageView(context: Context, attrs: AttributeSet?) : AppCompatImageView(context, attrs) {

    private val paint = Paint()
    private val paintBorder = Paint()
    private val borderWidth = 4.0f

    init {
        paint.isAntiAlias = true
        paintBorder.isAntiAlias = true
        paintBorder.color = Color.WHITE
    }

    override fun onDraw(canvas: Canvas) {
        val drawable = drawable ?: return
        val bitmap = (drawable as BitmapDrawable).bitmap
        val width = width
        val height = height

        val bitmapShader = BitmapShader(Bitmap.createScaledBitmap(bitmap, width, height, false), Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = bitmapShader

        val radius = width / 2f

        // Draw the circular image
        canvas.drawCircle(width / 2f, height / 2f, radius, paint)

        // Draw the border
        canvas.drawCircle(width / 2f, height / 2f, radius - borderWidth / 2, paintBorder)
    }
}

private fun loadProfileImage(userId: String, onResult: (String?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection("images")
        .whereEqualTo("user_id", userId)
        .get()
        .addOnSuccessListener { documents ->
            if (!documents.isEmpty) {
                val document = documents.first()
                val imageUrl = document.getString("profile_url")
                onResult(imageUrl)

                Log.e("Profile", "HAS DOC")
            } else {
                Log.e("Profile", "EMPTY DOC")
                onResult(null)
            }
        }
        .addOnFailureListener { exception ->
            Log.e("Profile", "Error getting documents: ", exception)
            onResult(null)
        }
}


private fun uploadImageToFirebase(uri: Uri, userId: String) {
    val storageRef = FirebaseStorage.getInstance().reference
    val imageRef = storageRef.child("images/${userId}.jpg")

    imageRef.putFile(uri)
        .addOnSuccessListener { taskSnapshot ->
            taskSnapshot.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                saveProfileImage(downloadUrl.toString(), userId)
            }
        }
        .addOnFailureListener { e ->
            Log.e("uploadImageToFirebase", "Error uploading", e)
        }
}


private fun saveProfileImage(imageUrl: String, userId: String) {
    val db = FirebaseFirestore.getInstance()
    val imageData = hashMapOf(
        "profile_url" to imageUrl,
        "user_id" to userId
    )

    db.collection("images")
        .add(imageData)
        .addOnSuccessListener { documentReference ->
            Log.d("saveProfileImage", "DocumentSnapshot added with ID: ${documentReference.id}")
        }
        .addOnFailureListener { e ->
            Log.e("saveProfileImage", "Error adding document", e)
        }
}

