package com.example.cs446_ece452_android_app.data

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.cs446_ece452_android_app.data.DestinationEntryStruct

fun calculatePath(destinations : SnapshotStateList<DestinationEntryStruct>): List<String>{
    val optimizedPath = mutableListOf<String>()

    //Get list of only destinations and not timeSpent
    destinations.forEach { entry ->
        optimizedPath.add(entry.destination)
    }



    return optimizedPath
}