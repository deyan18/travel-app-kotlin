package us.mis.acmemobile

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide


class TripDetailActivity : AppCompatActivity() {

    private lateinit var builder : AlertDialog.Builder
    lateinit var originTextView : TextView
    lateinit var destinationTextView : TextView
    lateinit var startDateTextView : TextView
    lateinit var endDateTextView : TextView
    lateinit var priceTextView : TextView
    lateinit var descriptionTextView : TextView
    lateinit var bookmarkImageView : ImageView
    lateinit var imgImageView : ImageView
    lateinit var purchaseButton : Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        setContentView(R.layout.activity_trip_detail)

        val trip = intent.getParcelableExtra<Trip>(Constants.CURRENT_TRIP)
        if(trip != null) {
            originTextView = findViewById<TextView>(R.id.originTextView)
            destinationTextView = findViewById<TextView>(R.id.destinationTextView)
            startDateTextView = findViewById<TextView>(R.id.startDateTextView)
            endDateTextView = findViewById<TextView>(R.id.endDateTextView)
            priceTextView = findViewById<TextView>(R.id.priceTextView)
            descriptionTextView = findViewById<TextView>(R.id.descriptionDetailTextView)
            bookmarkImageView = findViewById<ImageView>(R.id.bookmarkImageView)
            imgImageView = findViewById<ImageView>(R.id.imgDetailImageView)
            purchaseButton = findViewById<Button>(R.id.purchaseButton)

            checkPurchaseButton(trip)


            Glide.with(imgImageView.context).load(trip.imgURL).into(imgImageView)
            originTextView.text = trip.origin
            destinationTextView.text = trip.destination
            startDateTextView.text = SimpleDateFormat("MMM d yyyy").format(trip.startDate.time)
            endDateTextView.text = SimpleDateFormat("MMM d yyyy").format(trip.endDate.time)
            priceTextView.text = trip.price.toString() + " €"
            descriptionTextView.text = trip.description
            bookmarkImageView.setImageResource(if(TripSharedPreferences.getDefaultUser(this).bookmarkedTrips.contains(trip.id)) R.drawable.bookmark_fill else R.drawable.bookmark_border)

            bookmarkImageView.setOnClickListener{
                if(TripSharedPreferences.getDefaultUser(this).bookmarkedTrips.contains(trip.id)) {
                    TripSharedPreferences.removeBookmarkedTrip(this, trip.id)
                    bookmarkImageView.setImageResource(R.drawable.bookmark_border)
                } else {
                    TripSharedPreferences.bookmarkTrip(this, trip.id)
                    bookmarkImageView.setImageResource(R.drawable.bookmark_fill)
                }
            }
        }

    }

    fun checkPurchaseButton(trip: Trip){
        builder = AlertDialog.Builder(this)

        if(!TripSharedPreferences.getDefaultUser(this).purchasedTrips.contains(trip.id)) {
            purchaseButton.setOnClickListener {
                builder.setTitle("Confirm Purchase")
                    .setMessage("Do you want to purchase the trip from ${trip.origin} to ${trip.destination} for ${trip.price}€?")
                    .setCancelable(true)
                    .setPositiveButton("Yes") { dialogInterface, it ->
                        TripSharedPreferences.purchaseTrip(this, trip.id)
                        checkPurchaseButton(trip)
                    }
                    .setNegativeButton("No") { dialogInterface, it ->
                        dialogInterface.cancel()
                    }
                    .show()
            }
        }else{
            purchaseButton.background = getDrawable(R.drawable.purchase_button_deactivated)
            purchaseButton.setTextColor(getColor(R.color.button_color))
            purchaseButton.isEnabled = false
            purchaseButton.isClickable = false
            purchaseButton.text = "Purchased"
        }


    }

    fun setStatusBar(){
        //Not the best solution but I want the status bar to look cohesive with the app
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.acme_background)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}