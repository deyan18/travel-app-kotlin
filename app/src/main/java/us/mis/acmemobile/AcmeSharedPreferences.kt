package us.mis.acmemobile

import android.content.Context
import android.content.SharedPreferences
import android.icu.text.SimpleDateFormat
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TripSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        addDefaultUser(context, UserProvider.defaultUser)
        setTrips(context, TripProvider.trips)
        setCompactViewMode(context, false)
        setOriginQuery(context, "")
        setDestinationQuery(context, "")
        setStartDate(context, "")
        setEndDate(context, "")
        setPriceRangeStart(context, -1)
        setPriceRangeEnd(context, -1)
    }

    fun getAllTrips(context: Context): List<Trip> {
        val gson = Gson()
        val content = sharedPreferences.getString(Constants.TRIPS, "")

        val temp: MutableList<Trip> = if (content!!.isEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<List<Trip>>() {}.type
            gson.fromJson(content, type)
        }

        return temp
    }

    fun getFilteredTrips(context: Context): List<Trip> {
        val gson = Gson()
        val content = sharedPreferences.getString(Constants.TRIPS, "")

        var temp: List<Trip> = if (content!!.isEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<List<Trip>>() {}.type
            gson.fromJson(content, type)
        }

        //Search filters
        val originQuery = getOriginQuery(context)
        val destinationQuery = getDestinationQuery(context)

        temp = if (!originQuery.isNullOrBlank() && !destinationQuery.isNullOrBlank()) {
            // Filter the trips by origin and destination that contain the search query
            temp.filter { trip ->
                trip.origin.contains(originQuery, true) && trip.destination.contains(destinationQuery, true)
            }
        }else if (!originQuery.isNullOrBlank()) {
            temp.filter { trip ->
                trip.origin.contains(originQuery, true)
            }
        }else if (!destinationQuery.isNullOrBlank()) {
            temp.filter { trip ->
                trip.destination.contains(destinationQuery, true)
            }
        }else{
            temp
        }

        //Bookmarks filter
        temp = if (getBookmarksViewMode(context)) {
            temp.filter { it.id in getDefaultUser(context).bookmarkedTrips }
        } else {
            temp
        }

        //Date filter
        val startDate = getStartDate(context)
        val endDate = getEndDate(context)

        temp = if (!startDate.isNullOrBlank() && !endDate.isNullOrBlank()) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val start = sdf.parse(startDate)
            val end = sdf.parse(endDate)
            temp.filter { trip ->
                trip.startDate.time.after(start) && trip.endDate.time.before(end)
            }
        } else if (!startDate.isNullOrBlank()) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val start = sdf.parse(startDate)
            temp.filter { trip ->
                trip.startDate.time.after(start)
            }
        } else if (!endDate.isNullOrBlank()) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val end = sdf.parse(endDate)
            temp.filter { trip ->
                trip.endDate.time.before(end)
            }
        } else {
            temp
        }

        //Price filter
        val priceRangeStart = getPriceRangeStart(context)
        val priceRangeEnd = getPriceRangeEnd(context)

        temp = if (priceRangeStart != -1 && priceRangeEnd != -1) {
            temp.filter { trip ->
                trip.price >= priceRangeStart && trip.price <= priceRangeEnd
            }
        } else if (priceRangeStart != -1) {
            temp.filter { trip ->
                trip.price >= priceRangeStart
            }
        } else if (priceRangeEnd != -1) {
            temp.filter { trip ->
                trip.price <= priceRangeEnd
            }
        } else {
            temp
        }


        return temp
    }

    fun setTrips(context: Context, trips: List<Trip>) {
        val temp = trips.toMutableList()
        val gson = Gson()
        val dbs = gson.toJson(temp)
        editor.putString(Constants.TRIPS, dbs).apply()
        editor.apply()
    }


    fun bookmarkTrip(context: Context, tripID: String) {
        var temp = getDefaultUser(context)
        temp.bookmarkedTrips.add(tripID)
        val gson = Gson()
        val dbs = gson.toJson(temp)
        editor.putString(Constants.DEFAULT_USER, dbs).apply()
        editor.apply()
    }

    fun removeBookmarkedTrip(context: Context, tripID: String) {
        var temp = getDefaultUser(context)
        temp.bookmarkedTrips.remove(tripID)
        val gson = Gson()
        val dbs = gson.toJson(temp)
        editor.putString(Constants.DEFAULT_USER, dbs).apply()
        editor.apply()
    }

    fun purchaseTrip(context: Context, tripID: String) {
        var temp = getDefaultUser(context)
        temp.purchasedTrips.add(tripID)
        val gson = Gson()
        val dbs = gson.toJson(temp)
        editor.putString(Constants.DEFAULT_USER, dbs).apply()
        editor.apply()
    }

    fun getDefaultUser(context: Context): User {
        val gson = Gson()
        val content = sharedPreferences.getString(Constants.DEFAULT_USER, "")

        val temp: User = if (content!!.isEmpty()) {
            UserProvider.defaultUser
        } else {
            val type = object : TypeToken<User>() {}.type
            gson.fromJson(content, type)
        }
        return temp
    }

    fun addDefaultUser(context: Context, user: User) {
        val gson = Gson()
        val dbs = gson.toJson(user)
        editor.putString(Constants.DEFAULT_USER, dbs).apply()
        editor.apply()
    }

    fun setCompactViewMode(context: Context, compactViewOn: Boolean) {
        editor.putBoolean(Constants.COMPACT_VIEW_ON, compactViewOn).apply()
        editor.apply()
    }

    fun getCompactViewMode(context: Context): Boolean {
        return sharedPreferences.getBoolean(Constants.COMPACT_VIEW_ON, false)
    }

    fun setBookmarksViewMode(context: Context, bookmarksViewOn: Boolean) {
        editor.putBoolean(Constants.BOOKMARKS_VIEW_ON, bookmarksViewOn).apply()
        editor.apply()
    }

    fun getBookmarksViewMode(context: Context): Boolean {
        return sharedPreferences.getBoolean(Constants.BOOKMARKS_VIEW_ON, false)
    }

    fun setOriginQuery(context: Context, originQuery: String) {
        editor.putString(Constants.ORIGIN_QUERY, originQuery).apply()
        editor.apply()
    }

    fun getOriginQuery(context: Context): String {
        return sharedPreferences.getString(Constants.ORIGIN_QUERY, "")!!
    }

    fun setDestinationQuery(context: Context, destinationQuery: String) {
        editor.putString(Constants.DESTINATION_QUERY, destinationQuery).apply()
        editor.apply()
    }

    fun getDestinationQuery(context: Context): String {
        return sharedPreferences.getString(Constants.DESTINATION_QUERY, "")!!
    }

    fun setStartDate(context: Context, startDate: String) {
        editor.putString(Constants.START_DATE, startDate).apply()
        editor.apply()
    }

    fun getStartDate(context: Context): String {
        return sharedPreferences.getString(Constants.START_DATE, "")!!
    }

    fun setEndDate(context: Context, endDate: String) {
        editor.putString(Constants.END_DATE, endDate).apply()
        editor.apply()
    }

    fun getEndDate(context: Context): String {
        return sharedPreferences.getString(Constants.END_DATE, "")!!
    }

    fun setPriceRangeStart(context: Context, priceRangeStart: Int) {
        editor.putInt(Constants.PRICE_RANGE_START, priceRangeStart).apply()
        editor.apply()
    }

    fun getPriceRangeStart(context: Context): Int {
        return sharedPreferences.getInt(Constants.PRICE_RANGE_START, -1)
    }

    fun setPriceRangeEnd(context: Context, priceRangeEnd: Int) {
        editor.putInt(Constants.PRICE_RANGE_END, priceRangeEnd).apply()
        editor.apply()
    }

    fun getPriceRangeEnd(context: Context): Int {
        return sharedPreferences.getInt(Constants.PRICE_RANGE_END, -1)
    }
}