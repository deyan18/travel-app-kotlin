package us.mis.acmemobile

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object TripSharedPreferences {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(Constants.TRIPS_SP, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        addTrips(context, TripProvider.trips)
    }

    fun getAllTrips(context: Context): List<Trip> {
        val gson = Gson()
        val content = sharedPreferences.getString(Constants.TRIPS_SP, "")

        val temp: MutableList<Trip> = if (content!!.isEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<List<Trip>>() {}.type
            gson.fromJson(content, type)
        }
        return temp
    }

    fun addTrips(context: Context, trips: List<Trip>) {
        val temp = getAllTrips(context).toMutableList()
        temp.addAll(trips)
        val gson = Gson()
        val dbs = gson.toJson(temp)
        editor.putString(Constants.TRIPS_SP, dbs).apply()
        editor.apply()
    }
}