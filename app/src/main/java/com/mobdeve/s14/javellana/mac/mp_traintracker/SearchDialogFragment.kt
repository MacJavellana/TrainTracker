package com.mobdeve.s14.javellana.mac.mp_traintracker

import SearchAdapter
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.DialogFragment
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.mobdeve.s14.javellana.mac.mp_traintracker.databinding.FragmentSearchDialogBinding

class SearchDialogFragment(private val stations: List<Station>, private val onStationSelected: (LatLng) -> Unit) : DialogFragment() {

    private lateinit var binding: FragmentSearchDialogBinding
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            setGravity(Gravity.TOP)
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        setupRecyclerView()
        setupSearchView()
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(stations.toMutableList()) { station ->
            onStationSelected(station.location)
            dismiss()
        }
        binding.stationsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean = true

                override fun onQueryTextChange(newText: String?): Boolean {
                    filterStations(newText)
                    return true
                }
            })
            requestFocus()
        }
    }

    private fun filterStations(query: String?) {
        val filteredList = if (query.isNullOrBlank()) {
            stations.toMutableList()
        } else {
            stations.filter { it.name.contains(query, ignoreCase = true) }
        }
        searchAdapter.updateList(filteredList)
    }
}
