package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.location.Location
import com.google.android.gms.maps.model.LatLng

data class Train(
    val id: String,
    val line: String,
    val status: String,
    val location: LatLng,
    val nextStation: String,
    val estimatedTime: String,
    val distance: Int? = null
) {
    companion object {
        private const val AVERAGE_TRAIN_SPEED_MPS = 13.89 // 50 km/h in meters per second

        fun calculateEstimatedTime(currentLocation: LatLng, nextStationLocation: LatLng): String {
            val results = FloatArray(1)
            Location.distanceBetween(
                currentLocation.latitude,
                currentLocation.longitude,
                nextStationLocation.latitude,
                nextStationLocation.longitude,
                results
            )

            val distanceInMeters = results[0]
            val timeInSeconds = (distanceInMeters / AVERAGE_TRAIN_SPEED_MPS).toInt()
            val minutes = timeInSeconds / 60

            return when {
                minutes < 1 -> "Arriving"
                minutes == 1 -> "1 minute"
                else -> "$minutes minutes"
            }
        }
    }
}