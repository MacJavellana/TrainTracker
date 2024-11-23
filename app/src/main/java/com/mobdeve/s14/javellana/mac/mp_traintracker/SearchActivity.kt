package com.mobdeve.s14.javellana.mac.mp_traintracker

import SearchAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mobdeve.s14.javellana.mac.mp_traintracker.databinding.ActivitySearchBinding

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchAdapter: SearchAdapter
    private var stations: List<Station> = StationsData.allStations
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupSearchView()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        searchAdapter = SearchAdapter(stations.toMutableList()) { station ->
            val intent = Intent().apply {
                putExtra("latitude", station.location.latitude)
                putExtra("longitude", station.location.longitude)
            }
            setResult(RESULT_OK, intent)
            finish()
        }
        binding.stationsList.apply {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
    }

    private fun setupSearchView() {
        binding.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = true
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
