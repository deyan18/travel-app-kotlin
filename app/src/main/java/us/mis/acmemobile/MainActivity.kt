package us.mis.acmemobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import us.mis.acmemobile.adapter.TripAdapter


class MainActivity : AppCompatActivity() {

    lateinit var compactViewModeButton: Button
    lateinit var bookmarksViewModeButton: Button
    lateinit var filtersButton: Button
    lateinit var originSearchView: SearchView
    lateinit var destinationSearchView: SearchView
    lateinit var recyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        compactViewModeButton = findViewById<Button>(R.id.compactViewModeButton)
        bookmarksViewModeButton = findViewById<Button>(R.id.bookmarksViewModeButton)
        filtersButton = findViewById<Button>(R.id.filtersButton)
        originSearchView = findViewById(R.id.originSearchView)
        destinationSearchView = findViewById(R.id.destinationSearchView)

        filtersButton.setOnClickListener{
            // on below line we are creating a new bottom sheet dialog.
            val dialog = BottomSheetDialog(this)

            // on below line we are inflating a layout file which we have created.
            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)

            // on below line we are creating a variable for our button
            // which we are using to dismiss our dialog.
            val saveButton = view.findViewById<Button>(R.id.saveButton)
            val removeButton = view.findViewById<Button>(R.id.removeButton)


            saveButton.setOnClickListener {

                dialog.dismiss()
            }
            removeButton.setOnClickListener {

                dialog.dismiss()
            }
            dialog.setCancelable(false)
            dialog.setContentView(view)
            dialog.show()
        }


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