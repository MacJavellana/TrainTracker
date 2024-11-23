package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.content.Context
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging

class TrainMonitor(private val context: Context) {
    private val firebaseMessaging = FirebaseMessaging.getInstance()

    fun trackTrain(trainId: String) {
        firebaseMessaging.subscribeToTopic("train_$trainId")
            .addOnSuccessListener {
                saveTrackedTrain(trainId)
                showTrackingConfirmation()
            }
    }

    fun stopTracking(trainId: String) {
        firebaseMessaging.unsubscribeFromTopic("train_$trainId")
            .addOnSuccessListener {
                removeTrackedTrain(trainId)
            }
    }

    private fun saveTrackedTrain(trainId: String) {
        val prefs = context.getSharedPreferences("tracked_trains", Context.MODE_PRIVATE)
        prefs.edit().putBoolean(trainId, true).apply()
    }

    private fun removeTrackedTrain(trainId: String) {
        val prefs = context.getSharedPreferences("tracked_trains", Context.MODE_PRIVATE)
        prefs.edit().remove(trainId).apply()
    }

    private fun showTrackingConfirmation() {
        // Show a toast or snackbar confirmation
        Toast.makeText(context, "Now tracking this train", Toast.LENGTH_SHORT).show()
    }
}
