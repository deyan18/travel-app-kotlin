package us.mis.acmemobile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import us.mis.acmemobile.adapter.TripAdapter


class MainActivity : AppCompatActivity() {

    lateinit var compactViewModeButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compactViewModeButton = findViewById<Button>(R.id.compactViewModeButton)

        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }


    fun initRecyclerView() {
        checkCompactViewModeButton()
        val manager = GridLayoutManager(this, if(TripSharedPreferences.getCompactViewMode(this)) 2 else 1)
        val recyclerView = findViewById<RecyclerView>(R.id.tripsRecyclerView)
        recyclerView.layoutManager = manager
        recyclerView.adapter = TripAdapter(TripSharedPreferences.getAllTrips(this), this::onItemClicked, TripSharedPreferences.getCompactViewMode(this))
    }

    fun onItemClicked(trip: Trip) {
        val intent = Intent(this, TripDetailActivity::class.java)
        intent.putExtra(Constants.CURRENT_TRIP, trip)
        startActivity(intent)
        Toast.makeText(this, "Clicked on ${trip.origin}", Toast.LENGTH_SHORT).show()
    }

    fun checkCompactViewModeButton(){
        if(TripSharedPreferences.getCompactViewMode(this)){
            compactViewModeButton.background = getDrawable(R.drawable.search_filters_button_selected)
            compactViewModeButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        }else{
            compactViewModeButton.background = getDrawable(R.drawable.search_filters_button)
            compactViewModeButton.setTextColor(ContextCompat.getColor(this, R.color.button_color))

        }

        compactViewModeButton.setOnClickListener{
            TripSharedPreferences.setCompactViewMode(this, !TripSharedPreferences.getCompactViewMode(this))
            initRecyclerView()
        }
    }

}