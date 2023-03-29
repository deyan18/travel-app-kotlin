package us.mis.acmemobile

import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide


class TripDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trip_detail)

        val trip = intent.getParcelableExtra<Trip>(Constants.CURRENT_TRIP)
        if(trip != null) {
            val originDetailTextView = findViewById<TextView>(R.id.originDetailTextView)
            val destinationDetailTextView = findViewById<TextView>(R.id.destinationDetailTextView)
            val startDateDetailTextView = findViewById<TextView>(R.id.startDateDetailTextView)
            val endDateDetailTextView = findViewById<TextView>(R.id.endDateDetailTextView)
            val priceDetailTextView = findViewById<TextView>(R.id.priceDetailTextView)
            val descriptionDetailTextView = findViewById<TextView>(R.id.descriptionDetailTextView)
            val bookmarkDetailImageView = findViewById<ImageView>(R.id.bookmarkDetailImageView)
            val imgDetailImageView = findViewById<ImageView>(R.id.imgDetailImageView)


            Glide.with(imgDetailImageView.context).load(trip.imgURL).into(imgDetailImageView)
            originDetailTextView.text = trip.origin
            destinationDetailTextView.text = trip.destination
            startDateDetailTextView.text = SimpleDateFormat("MMM d YY").format(trip.startDate.time)
            endDateDetailTextView.text = SimpleDateFormat("MMM d YY").format(trip.endDate.time)
            priceDetailTextView.text = trip.price.toString()
            descriptionDetailTextView.text = trip.description
            bookmarkDetailImageView.setImageResource(if(TripSharedPreferences.getDefaultUser(this).bookmarkedTrips.contains(trip.id)) R.drawable.bookmark_fill else R.drawable.bookmark_border)

            bookmarkDetailImageView.setOnClickListener{
                if(TripSharedPreferences.getDefaultUser(this).bookmarkedTrips.contains(trip.id)) {
                    TripSharedPreferences.removeBookmarkedTrip(this, trip.id)
                    bookmarkDetailImageView.setImageResource(R.drawable.bookmark_border)
                } else {
                    TripSharedPreferences.bookmarkTrip(this, trip.id)
                    bookmarkDetailImageView.setImageResource(R.drawable.bookmark_fill)
                }
            }
        }

    }
}