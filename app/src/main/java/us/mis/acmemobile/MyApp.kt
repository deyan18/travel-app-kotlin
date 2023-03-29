package us.mis.acmemobile

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TripSharedPreferences.init(this)
    }
}