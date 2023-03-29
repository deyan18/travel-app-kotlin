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

        val trip = intent.getParcelableExtra<Trip>(Constants.TRIP_DETAIL)
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
            bookmarkDetailImageView.setImageResource(if(trip.boomarked) R.drawable.bookmark_fill else R.drawable.bookmark_border)
        }

    }
}