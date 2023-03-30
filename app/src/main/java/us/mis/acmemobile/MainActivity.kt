package us.mis.acmemobile

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import us.mis.acmemobile.adapter.TripAdapter


class MainActivity : AppCompatActivity() {

    lateinit var compactViewModeButton: Button
    lateinit var bookmarksViewModeButton: Button
    lateinit var originSearchView: SearchView
    lateinit var destinationSearchView: SearchView
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compactViewModeButton = findViewById<Button>(R.id.compactViewModeButton)
        bookmarksViewModeButton = findViewById<Button>(R.id.bookmarksViewModeButton)
        originSearchView = findViewById(R.id.originSearchView)
        destinationSearchView = findViewById(R.id.destinationSearchView)


        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }


    fun initRecyclerView() {
        checkCompactViewModeButton()
        checkBookmarksButton()
        checkSearch()

        val manager = GridLayoutManager(this, if(TripSharedPreferences.getCompactViewMode(this)) 2 else 1)
        recyclerView = findViewById<RecyclerView>(R.id.tripsRecyclerView)
        recyclerView.layoutManager = manager
        updateRecyclerView()

    }

    fun onItemClicked(trip: Trip) {
        val intent = Intent(this, TripDetailActivity::class.java)
        intent.putExtra(Constants.CURRENT_TRIP, trip)
        startActivity(intent)
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

    fun checkBookmarksButton(){
        if(TripSharedPreferences.getBookmarksViewMode(this)){
            bookmarksViewModeButton.background = getDrawable(R.drawable.search_filters_button_selected)
            bookmarksViewModeButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        }else{
            bookmarksViewModeButton.background = getDrawable(R.drawable.search_filters_button)
            bookmarksViewModeButton.setTextColor(ContextCompat.getColor(this, R.color.button_color))
        }

        bookmarksViewModeButton.setOnClickListener{
            TripSharedPreferences.setBookmarksViewMode(this, !TripSharedPreferences.getBookmarksViewMode(this))
            initRecyclerView()
        }
    }

    fun checkSearch(){
        TripSharedPreferences.setOriginQuery(this, originSearchView.query.toString())
        TripSharedPreferences.setDestinationQuery(this, destinationSearchView.query.toString())

        originSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Call a method to filter the data based on the new search query
                TripSharedPreferences.setOriginQuery(this@MainActivity, newText.toString())
                updateRecyclerView()
                return true
            }
        })

        destinationSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Call a method to filter the data based on the new search query
                TripSharedPreferences.setDestinationQuery(this@MainActivity, newText.toString())
                updateRecyclerView()
                return true
            }
        })
    }

    fun updateRecyclerView(){
        recyclerView.adapter = TripAdapter(TripSharedPreferences.getFilteredTrips(this), this::onItemClicked, TripSharedPreferences.getCompactViewMode(this))
    }


}