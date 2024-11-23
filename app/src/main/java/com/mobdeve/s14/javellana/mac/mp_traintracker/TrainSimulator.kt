package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.os.Handler
import android.os.Looper
import com.google.firebase.database.FirebaseDatabase
import java.util.Random

class TrainSimulator {
    private val database = FirebaseDatabase.getInstance()
    private val trainsRef = database.getReference("trains")
    private val random = Random()

    private val dummyTrains = listOf(
        "LRT1_Train1", "LRT1_Train2", "LRT1_Train3",
        "LRT2_Train1", "LRT2_Train2",
        "MRT3_Train1", "MRT3_Train2", "MRT3_Train3"
    )

    fun startSimulation() {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                updateTrainLocations()
                handler.postDelayed(this, 30000) // Update every 30 seconds
            }
        }, 0)
    }

    private fun updateTrainLocations() {
        dummyTrains.forEach { trainId ->
            val line = trainId.split("_")[0]
            val stations = when(line) {
                "LRT1" -> StationsData.lrt1Stations
                "LRT2" -> StationsData.lrt2Stations
                else -> StationsData.mrt3Stations
            }

            val randomStation = stations[random.nextInt(stations.size)]
            val nextStation = stations[(stations.indexOf(randomStation) + 1) % stations.size]

            val trainData = TrainData(
                trainId = trainId,
                line = line,
                currentStation = randomStation.name,
                nextStation = nextStation.name,
                status = getRandomStatus(),
                location = TrainLocation(
                    latitude = randomStation.location.latitude,
                    longitude = randomStation.location.longitude
                ),
                lastUpdated = System.currentTimeMillis()
            )

            trainsRef.child(trainId).setValue(trainData)
        }
    }

    private fun getRandomStatus(): String {
        return listOf("ON_TIME", "DELAYED", "STOPPED").random()
    }
}
