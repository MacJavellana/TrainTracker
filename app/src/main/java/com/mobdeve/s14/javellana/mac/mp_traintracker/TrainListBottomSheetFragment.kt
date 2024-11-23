package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mobdeve.s14.javellana.mac.mp_traintracker.databinding.FragmentTrainListBottomSheetBinding
class TrainListBottomSheetFragment(
    private val onTrainSelected: (LatLng) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentTrainListBottomSheetBinding
    private val firebaseService = FirebaseService()
    private lateinit var locationService: LocationService
    private var userLocation: Location? = null
    private var currentFilter: TrainFilter? = null
    private lateinit var trainMonitor: TrainMonitor


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trainMonitor = TrainMonitor(requireContext())
        locationService = LocationService(requireContext())
        setupRecyclerView()
        setupFilterUI()
        getUserLocationAndObserveTrains()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrainListBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun applyFilter(filter: TrainFilter) {
        currentFilter = filter
        observeTrains()
    }
    private fun getUserLocationAndObserveTrains() {
        locationService.getCurrentLocation { location ->
            userLocation = location
            observeTrains()
        }
    }

    private fun observeTrains() {
        firebaseService.observeTrainUpdates { trainDataList ->
            var trains = trainDataList.mapNotNull { trainData ->
                try {
                    val nextStationLocation = StationsData.allStations.find {
                        it.name == trainData.nextStation
                    }?.location ?: return@mapNotNull null

                    val distance = if (userLocation != null) {
                        val trainLocation = Location("").apply {
                            latitude = trainData.location.latitude
                            longitude = trainData.location.longitude
                        }
                        userLocation?.distanceTo(trainLocation)?.toInt()
                    } else {
                        null
                    }

                    Train(
                        id = trainData.trainId,
                        line = trainData.line,
                        status = trainData.status,
                        location = LatLng(trainData.location.latitude, trainData.location.longitude),
                        nextStation = trainData.nextStation,
                        estimatedTime = Train.calculateEstimatedTime(
                            LatLng(trainData.location.latitude, trainData.location.longitude),
                            nextStationLocation
                        ),
                        distance = distance
                    )
                } catch (e: Exception) {
                    Log.e("TrainListFragment", "Error mapping train data", e)
                    null
                }
            }.let { trainList ->
                if (userLocation != null) {
                    trainList.filter { train ->
                        train.distance?.let { it <= 10000 } ?: true
                    }.sortedBy {
                        it.distance ?: Int.MAX_VALUE
                    }
                } else {
                    trainList
                }
            }

            currentFilter?.let { filter ->
                trains = trains.applyFilters(filter)
            }

            binding.recyclerView.adapter = TrainAdapter(
                trainList = trains,
                onTrainClick = onTrainSelected,
                onTrackTrain = { trainId ->
                    trainMonitor.trackTrain(trainId)
                }
            )
        }
    }






    private fun setupFilterUI() {
        binding.btnLrt1.setOnClickListener { applyFilter(TrainFilter(line = "LRT1")) }
        binding.btnLrt2.setOnClickListener { applyFilter(TrainFilter(line = "LRT2")) }
        binding.btnMrt3.setOnClickListener { applyFilter(TrainFilter(line = "MRT3")) }
    }


    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = TrainAdapter(
            trainList = emptyList(),
            onTrainClick = onTrainSelected,
            onTrackTrain = { trainId ->
                trainMonitor.trackTrain(trainId)
            }
        )
    }

}
