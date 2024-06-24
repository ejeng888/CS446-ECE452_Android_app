package com.example.cs446_ece452_android_app
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        // Set user details
        val username: TextView = findViewById(R.id.username)
        val email: TextView = findViewById(R.id.email)
        val address: TextView = findViewById(R.id.address)
        username.text = "John Doe"
        email.text = "JohnDoe@gmail.com"
        address.text = "Japan"
    }
}
