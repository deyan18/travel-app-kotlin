package us.mis.acmemobile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import us.mis.acmemobile.adapter.TripAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    fun initRecyclerView() {

        val manager = GridLayoutManager(this, 3)
        val decoration = DividerItemDecoration(this, manager.orientation)
        val recyclerView = findViewById<RecyclerView>(R.id.tripsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TripAdapter(TripSharedPreferences.getAllTrips(this), this::onItemClicked)
        recyclerView.addItemDecoration(decoration)
    }

    fun onItemClicked(trip: Trip) {
        val intent = Intent(this, TripDetailActivity::class.java)
        intent.putExtra(Constants.CURRENT_TRIP, trip)
        startActivity(intent)
        Toast.makeText(this, "Clicked on ${trip.origin}", Toast.LENGTH_SHORT).show()
    }

}