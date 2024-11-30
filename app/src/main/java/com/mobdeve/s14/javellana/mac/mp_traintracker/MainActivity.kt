package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.mobdeve.s14.javellana.mac.mp_traintracker.databinding.ActivityMainBinding
import android.graphics.Color
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.Manifest
import android.util.Log

class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private val LOCATION_PERMISSION_REQUEST = 1001
    private lateinit var binding: ActivityMainBinding
    private lateinit var googleMap: GoogleMap
    private var lrt1: List<Station> = StationsData.lrt1Stations
    private var lrt2: List<Station> = StationsData.lrt2Stations
    private var mrt3: List<Station> = StationsData.mrt3Stations
    private val searchActivityRequestCode = 100
    private var stations: List<Station> = StationsData.allStations

    private val firebaseService = FirebaseService()
    //private val trainSimulator = TrainSimulator()
    private val trainMarkers = mutableMapOf<String, Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermissions()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigationDrawer()
        setupMapFragment()
        setupClickListeners()

        // Start train simulation and observe updates
        //trainSimulator.startSimulation()
        observeTrainUpdates()
    }



    private fun requestLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupMapSettings()
            }
        }
    }



    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, binding.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
    }

    private fun setupNavigationDrawer() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_settings -> handleSettings()
                R.id.nav_help_support -> handleHelpSupport()
                R.id.nav_feedback -> handleFeedback()
                R.id.nav_about -> handleAbout()
                R.id.nav_legal -> handleLegal()
            }
            binding.drawerLayout.closeDrawers()
            true
        }
    }

    private fun setupMapFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    private fun setupClickListeners() {
        binding.searchBtn.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivityForResult(intent, searchActivityRequestCode)
        }

        binding.viewListBtn.setOnClickListener {
            showTrainListBottomSheet()
        }
    }

    private fun observeTrainUpdates() {
        firebaseService.observeTrainUpdates { trains ->
            updateTrainMarkers(trains)
        }
    }
    private fun showTrainListBottomSheet() {
        val trainListBottomSheet = TrainListBottomSheetFragment { location ->
            // Move the camera to the selected train's location
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
        }
        trainListBottomSheet.show(supportFragmentManager, "TrainListBottomSheet")
    }

    private fun updateTrainMarkers(trains: List<TrainData>) {
        // Clear old markers first
        trainMarkers.values.forEach { it.remove() }
        trainMarkers.clear()

        trains.forEach { train ->
            val location = LatLng(train.location.latitude, train.location.longitude)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("${train.trainId} - ${train.status}")
                    .snippet("Next Station: ${train.nextStation}")
                    .icon(getBitmapDescriptorForTrain(train.line))
            )
            marker?.let { trainMarkers[train.trainId] = it }
        }
    }


    private fun createTrainMarker(train: TrainData): Marker {
        return googleMap.addMarker(
            MarkerOptions()
                .position(LatLng(train.location.latitude, train.location.longitude))
                .title(train.trainId)
                .icon(getBitmapDescriptorForTrain(train.line))
        )!!
    }

    private fun getBitmapDescriptorForTrain(line: String): BitmapDescriptor {
        return when(line) {
            "LRT1" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            "LRT2" -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
            else -> BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        drawTrainLines()
        setupMapSettings()
    }

    private fun drawTrainLines() {
        googleMap.addPolyline(PolylineOptions().addAll(lrt1.map { it.location }).color(Color.RED))
        googleMap.addPolyline(PolylineOptions().addAll(lrt2.map { it.location }).color(Color.BLUE))
        googleMap.addPolyline(PolylineOptions().addAll(mrt3.map { it.location }).color(Color.GREEN))

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(14.5995, 120.9842), 12f))
    }

    private fun setupMapSettings() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.apply {
                isMyLocationEnabled = true
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isCompassEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
        } else {
            Log.d("MainActivity", "Location features disabled - permission not granted")
            googleMap.apply {
                uiSettings.isZoomControlsEnabled = true
                uiSettings.isCompassEnabled = true
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == searchActivityRequestCode && resultCode == RESULT_OK) {
            data?.let {
                val latitude = it.getDoubleExtra("latitude", 0.0)
                val longitude = it.getDoubleExtra("longitude", 0.0)
                val location = LatLng(latitude, longitude)

                // Smooth camera animation to the selected location
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 16f),
                    1000,
                    null
                )
            }
        }
    }




    // Handle navigation menu actions
    private fun handleSettings() { /* Implementation */ }
    private fun handleHelpSupport() { /* Implementation */ }
    private fun handleFeedback() { /* Implementation */ }
    private fun handleAbout() { /* Implementation */ }
    private fun handleLegal() { /* Implementation */ }
}
