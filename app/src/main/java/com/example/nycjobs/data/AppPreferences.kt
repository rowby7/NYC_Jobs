package com.example.nycjobs.data

import android.content.Context
import android.content.SharedPreferences

/**
 * Interface for app shared preferences.
 */
interface AppSharedPreferences {
    fun getSharedPreferences(): SharedPreferences
}

/**
 * Implementation of SharedPreferences for storing app preferences.
 */
class AppPreferences(private val context: Context) : AppSharedPreferences {
    private val appPreferencesKey = "app_prefs"

    override fun getSharedPreferences(): SharedPreferences {
        return context.getSharedPreferences(appPreferencesKey, Context.MODE_PRIVATE)
    }
}
