package us.mis.acmemobile

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import us.mis.acmemobile.adapter.TripAdapter
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var compactViewModeButton: Button
    lateinit var bookmarksViewModeButton: Button
    lateinit var filtersButton: Button
    lateinit var originSearchView: SearchView
    lateinit var destinationSearchView: SearchView
    lateinit var recyclerView: RecyclerView
    lateinit var endDateTextView: TextView
    lateinit var startDateTextView: TextView
    lateinit var endDateImageView: ImageView
    lateinit var startDateImageView: ImageView
    lateinit var priceRangeSlider: RangeSlider


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStatusBar()
        setContentView(R.layout.activity_main)
        compactViewModeButton = findViewById<Button>(R.id.compactViewModeButton)
        bookmarksViewModeButton = findViewById<Button>(R.id.bookmarksViewModeButton)
        filtersButton = findViewById<Button>(R.id.filtersButton)
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
        checkFilters()

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
            bookmarksViewModeButton.setTextColor(ContextCompat.getColor(this, R.color.button_color))
            bookmarksViewModeButton.setBackgroundResource(R.drawable.search_filters_button)
            bookmarksViewModeButton.setAllowClickWhenDisabled(true)
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

    fun checkFilters() {
        checkFiltersButton()

        filtersButton.setOnClickListener{
            val startDateSaved = TripSharedPreferences.getStartDate(this)
            val endDateSaved = TripSharedPreferences.getEndDate(this)

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val saveButton = view.findViewById<Button>(R.id.saveButton)
            val removeButton = view.findViewById<Button>(R.id.removeButton)
            endDateTextView = view.findViewById(R.id.endDateTextView)
            startDateTextView = view.findViewById(R.id.startDateTextView)
            endDateImageView = view.findViewById(R.id.endDateImageView)
            startDateImageView = view.findViewById(R.id.startDateImageView)
            priceRangeSlider = view.findViewById(R.id.sliderRange)

            //Get lowest trip price
            val trips = TripSharedPreferences.getAllTrips(this)

            // Set minimum and maximum values
            priceRangeSlider.valueFrom = trips.minOf { it.price.toFloat() }
            priceRangeSlider.valueTo = trips.maxOf { it.price.toFloat() }

            if(TripSharedPreferences.getPriceRangeStart(this) != -1 && TripSharedPreferences.getPriceRangeEnd(this) != -1) {
                priceRangeSlider.setValues(
                    TripSharedPreferences.getPriceRangeStart(this).toFloat(),
                    TripSharedPreferences.getPriceRangeEnd(this).toFloat()
                )
            }else{
                priceRangeSlider.setValues(
                    trips.minOf { it.price.toFloat() },
                    trips.maxOf { it.price.toFloat() }
                )
            }

            priceRangeSlider.labelBehavior = LabelFormatter.LABEL_VISIBLE

            priceRangeSlider.setLabelFormatter { value ->
                value.toInt().toString() + "€"
            }

            endDateTextView.text = endDateSaved
            startDateTextView.text = startDateSaved

            val calendar = Calendar.getInstance()
            var yyStart = calendar.get(Calendar.YEAR)
            var mmStart = calendar.get(Calendar.MONTH)
            var ddStart = calendar.get(Calendar.DAY_OF_MONTH)
            var yyEnd = calendar.get(Calendar.YEAR)
            var mmEnd = calendar.get(Calendar.MONTH)
            var ddEnd = calendar.get(Calendar.DAY_OF_MONTH)

            if(startDateSaved != ""){
                val startDate = startDateSaved.split("/")
                yyStart = startDate[2].toInt()
                mmStart = startDate[1].toInt() - 1
                ddStart = startDate[0].toInt()
            }

            if(endDateSaved != ""){
                val endDate = endDateSaved.split("/")
                yyEnd = endDate[2].toInt()
                mmEnd = endDate[1].toInt() - 1
                ddEnd = endDate[0].toInt()
            }

            startDateImageView.setOnClickListener{
                val today = Calendar.getInstance()

                val datePickerDialog = DatePickerDialog(
                    this,
                    { datePicker, year, month, day ->
                        startDateTextView.text = "$day/${month + 1}/$year"
                        yyStart = year
                        mmStart = month
                        ddStart = day
                    },
                    yyStart, mmStart, ddStart
                )
                datePickerDialog.datePicker.minDate = today.timeInMillis

                datePickerDialog.show()
            }

            endDateImageView.setOnClickListener{
                val today = Calendar.getInstance()

                val datePickerDialog = DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                        endDateTextView.text = "$day/${month + 1}/$year"
                        yyEnd = year
                        mmEnd = month
                        ddEnd = day
                    },
                    yyEnd, mmEnd, ddEnd
                )

                if (yyStart != 0 && mmStart != 0 && ddStart != 0) {
                    val minDate = Calendar.getInstance().apply {
                        set(yyStart, mmStart, ddStart, 0, 0, 0)
                    }.timeInMillis
                    datePickerDialog.datePicker.minDate = minDate
                } else {
                    datePickerDialog.datePicker.minDate = today.timeInMillis
                }

                datePickerDialog.show()
            }

            saveButton.setOnClickListener{
                TripSharedPreferences.setStartDate(this, startDateTextView.text.toString())
                TripSharedPreferences.setEndDate(this, endDateTextView.text.toString())
                TripSharedPreferences.setPriceRangeStart(this, priceRangeSlider.values[0].toInt())
                TripSharedPreferences.setPriceRangeEnd(this, priceRangeSlider.values[1].toInt())
                updateRecyclerView()
                checkFiltersButton()
                dialog.dismiss()
            }

            removeButton.setOnClickListener{
                TripSharedPreferences.setStartDate(this, "")
                TripSharedPreferences.setEndDate(this, "")
                TripSharedPreferences.setPriceRangeStart(this, -1)
                TripSharedPreferences.setPriceRangeEnd(this, -1)
                updateRecyclerView()
                checkFiltersButton()
                dialog.dismiss()
            }

            dialog.setContentView(view)
            dialog.show()
        }
    }

    fun checkFiltersButton(){
        if(TripSharedPreferences.getStartDate(this) == "" && TripSharedPreferences.getEndDate(this) == "" && TripSharedPreferences.getPriceRangeStart(this) == -1 && TripSharedPreferences.getPriceRangeEnd(this) == -1){
            filtersButton.background = getDrawable(R.drawable.search_filters_button)
            filtersButton.setTextColor(ContextCompat.getColor(this, R.color.button_color))       }else{
            filtersButton.background = getDrawable(R.drawable.search_filters_button_selected)
            filtersButton.setTextColor(ContextCompat.getColor(this, R.color.white))
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