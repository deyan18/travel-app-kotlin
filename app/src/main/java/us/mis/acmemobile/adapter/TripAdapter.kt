package us.mis.acmemobile.adapter

import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import us.mis.acmemobile.R
import us.mis.acmemobile.Trip
import us.mis.acmemobile.TripSharedPreferences

class TripAdapter(private val tripList: List<Trip>, private val onClickListener:(Trip) -> Unit, val compactViewOn: Boolean): RecyclerView.Adapter<TripViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TripViewHolder(layoutInflater.inflate(if(compactViewOn)R.layout.trip_item_compact else R.layout.trip_item, parent, false))
    }

    override fun getItemCount(): Int = tripList.size

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.render(trip, onClickListener)
    }
}