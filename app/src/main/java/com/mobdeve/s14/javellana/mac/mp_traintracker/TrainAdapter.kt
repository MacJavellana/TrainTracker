package com.mobdeve.s14.javellana.mac.mp_traintracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng

class TrainAdapter(
    private val trainList: List<Train>,
    private val onTrainClick: (LatLng) -> Unit,
    private val onTrackTrain: (String) -> Unit
) : RecyclerView.Adapter<TrainAdapter.TrainViewHolder>() {

    class TrainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationName: TextView = view.findViewById(R.id.station_name)
        val trainInfo: TextView = view.findViewById(R.id.train_info)
        val distance: TextView = view.findViewById(R.id.distance)
        val trackButton: ImageButton = view.findViewById(R.id.track_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.train_item, parent, false)
        return TrainViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrainViewHolder, position: Int) {
        val train = trainList[position]

        holder.stationName.text = train.nextStation
        holder.trainInfo.text = "${train.estimatedTime} - ${train.status}"

        holder.distance.text = train.distance?.let { distance ->
            when {
                distance < 1000 -> "${distance}m"
                else -> String.format("%.1f km", distance / 1000.0)
            }
        } ?: ""

        holder.itemView.setOnClickListener {
            onTrainClick(train.location)
        }

        holder.trackButton.setOnClickListener {
            onTrackTrain(train.id)
        }
    }


    override fun getItemCount() = trainList.size
}
