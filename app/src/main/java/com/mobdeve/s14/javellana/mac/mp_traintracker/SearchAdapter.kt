import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s14.javellana.mac.mp_traintracker.R
import com.mobdeve.s14.javellana.mac.mp_traintracker.Station
import com.mobdeve.s14.javellana.mac.mp_traintracker.StationsData

class SearchAdapter(
    private var stations: MutableList<Station>,
    private val onStationClick: (Station) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val stationName: TextView = view.findViewById(R.id.station_name)
        val stationLine: TextView = view.findViewById(R.id.station_line)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_station, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val station = stations[position]
        holder.stationName.text = station.name

        // Determine which line the station belongs to
        val lineName = when {
            StationsData.lrt1Stations.contains(station) -> "LRT-1 Line"
            StationsData.lrt2Stations.contains(station) -> "LRT-2 Line"
            else -> "MRT-3 Line"
        }
        holder.stationLine.text = lineName

        holder.itemView.setOnClickListener { onStationClick(station) }
    }

    override fun getItemCount() = stations.size

    fun updateList(newList: List<Station>) {
        stations.clear()
        stations.addAll(newList)
        notifyDataSetChanged()
    }
}
