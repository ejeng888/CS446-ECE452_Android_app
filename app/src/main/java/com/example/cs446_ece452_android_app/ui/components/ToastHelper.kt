package com.example.cs446_ece452_android_app.ui.components

import android.content.Context
import android.widget.Toast

fun toastHelper(context: Context, toastMessage: String) {
    Toast.makeText(
        context,
        toastMessage,
        Toast.LENGTH_SHORT,
    ).show()
}