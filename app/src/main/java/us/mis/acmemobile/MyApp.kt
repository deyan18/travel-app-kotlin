package us.mis.acmemobile

import android.app.Application
import android.os.Build
import android.view.WindowManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        TripSharedPreferences.init(this)


    }
}