package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FirebaseService {
    private val database = FirebaseDatabase.getInstance()
    private val trainsRef = database.getReference("trains")

    fun observeTrainUpdates(onTrainUpdate: (List<TrainData>) -> Unit) {
        trainsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val trains = snapshot.children.mapNotNull {
                    it.getValue(TrainData::class.java)
                }
                onTrainUpdate(trains)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseService", "Error: ${error.message}")
            }
        })
    }
}