package us.mis.acmemobile.adapter

import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import us.mis.acmemobile.*

class TripViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val origin = view.findViewById<TextView>(R.id.originTextView)
    val destination = view.findViewById<TextView>(R.id.destinationTextView)
    val startDate = view.findViewById<TextView>(R.id.startDateTextView)
    val endDate = view.findViewById<TextView>(R.id.endDateTextView)
    val price = view.findViewById<TextView>(R.id.priceTextView)
    val bookmark = view.findViewById<ImageView>(R.id.bookmarkImageView)
    val img = view.findViewById<ImageView>(R.id.tripImageView)


    fun render(trip: Trip, onClickListener: (Trip) -> Unit) {
        origin.text = trip.origin
        destination.text = trip.destination
        val startDateString = SimpleDateFormat("MMM d").format(trip.startDate.time)
        startDate.text = startDateString
        val endDateString = SimpleDateFormat("MMM d").format(trip.endDate.time)
        endDate.text = endDateString
        price.text = trip.price.toString() + "€"
        bookmark.setImageResource(if(TripSharedPreferences.getDefaultUser(itemView.context).bookmarkedTrips.contains(trip.id)) R.drawable.bookmark_fill else R.drawable.bookmark_border)
        Glide.with(itemView.context).load(trip.imgURL).into(img)

        img.setOnClickListener {
            onClickListener(trip)
        }

        bookmark.setOnClickListener{
            if(TripSharedPreferences.getDefaultUser(itemView.context).bookmarkedTrips.contains(trip.id)) {
                TripSharedPreferences.removeBookmarkedTrip(itemView.context, trip.id)
                bookmark.setImageResource(R.drawable.bookmark_border)
            } else {
                TripSharedPreferences.bookmarkTrip(itemView.context, trip.id)
                bookmark.setImageResource(R.drawable.bookmark_fill)
            }
        }
    }

}