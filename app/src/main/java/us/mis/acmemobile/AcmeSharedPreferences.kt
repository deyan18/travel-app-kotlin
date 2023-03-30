package us.mis.acmemobile

import android.content.Context
import android.content.SharedPreferences
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

        return if (getBookmarksViewMode(context)) {
            temp.filter { it.id in getDefaultUser(context).bookmarkedTrips }
        } else {
            temp
        }
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
}