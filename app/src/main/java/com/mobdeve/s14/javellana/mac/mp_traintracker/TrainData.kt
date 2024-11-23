package com.mobdeve.s14.javellana.mac.mp_traintracker

data class TrainData(
    val trainId: String = "",
    val line: String = "",
    val currentStation: String = "",
    val nextStation: String = "",
    val status: String = "",
    val estimatedTime: String = "",
    val location: TrainLocation = TrainLocation(),
    val distance: Int = 0,
    val lastUpdated: Long = System.currentTimeMillis()
)

data class TrainLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)
