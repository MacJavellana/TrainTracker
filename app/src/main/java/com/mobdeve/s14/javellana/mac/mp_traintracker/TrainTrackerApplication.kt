package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class TrainTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}