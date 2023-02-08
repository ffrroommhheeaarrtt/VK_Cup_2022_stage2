package com.example.cup2.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.ui.graphics.vector.ImageVector

object SampleData {

    val quizOptions = listOf(
        "True",
        "False",
        "False",
        "False",
    )

    val matchedElements = listOf(
        "Forest" to MyIcon(Icons.Filled.Forest.name, Icons.Filled.Forest),
        "Airplane" to MyIcon(Icons.Filled.Flight.name, Icons.Filled.Flight),
        "Rocket" to MyIcon(Icons.Filled.RocketLaunch.name, Icons.Filled.RocketLaunch),
        "House" to MyIcon(Icons.Filled.House.name, Icons.Filled.House),
    )
}

data class MyIcon(val name: String, val imageVector: ImageVector)