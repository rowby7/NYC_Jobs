package com.example.nycjobs

import android.app.Application
import com.example.nycjobs.data.AppContainer
import com.example.nycjobs.data.DefaultAppContainer

/**
 * Main application class for NYC Open Jobs.
 * Initializes the app container on startup.
 */
class NYCOpenJobsApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
