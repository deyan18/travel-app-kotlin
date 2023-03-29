package us.mis.acmemobile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import us.mis.acmemobile.adapter.TripAdapter


class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    fun initRecyclerView() {

        sharedPreferences = getSharedPreferences(Constants.TRIP_DETAIL, MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val manager = GridLayoutManager(this, 3)
        val decoration = DividerItemDecoration(this, manager.orientation)
        val recyclerView = findViewById<RecyclerView>(R.id.tripsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TripAdapter(TripSharedPreferences.getAllTrips(this), this::onItemClicked)
        recyclerView.addItemDecoration(decoration)
    }

    fun onItemClicked(trip: Trip) {
        val intent = Intent(this, TripDetailActivity::class.java)
        intent.putExtra(Constants.TRIP_DETAIL, trip)
        startActivity(intent)
        Toast.makeText(this, "Clicked on ${trip.origin}", Toast.LENGTH_SHORT).show()
    }

}