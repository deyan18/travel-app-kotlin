package us.mis.acmemobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import us.mis.acmemobile.adapter.TripAdapter
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    fun initRecyclerView() {
        val manager = LinearLayoutManager(this)
        val decoration = DividerItemDecoration(this, manager.orientation)
        val recyclerView = findViewById<RecyclerView>(R.id.tripsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = TripAdapter(TripProvider.tripList, this::onItemClicked)
        recyclerView.addItemDecoration(decoration)
    }

    fun onItemClicked(trip: Trip) {
        Toast.makeText(this, "Clicked on ${trip.origin}", Toast.LENGTH_SHORT).show()
    }
}