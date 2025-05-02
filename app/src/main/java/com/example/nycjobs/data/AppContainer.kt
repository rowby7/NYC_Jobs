package com.example.nycjobs.data

import android.content.Context
import android.util.Log
import com.example.nycjobs.api.AppRemoteApis

interface AppContainer {
    val appRepository: AppRepository
}

/**
 * Default App Container
 * This class defines the default app container.
 * The container is responsible for providing the app repository.
 * @param context the context of the app
 */
class DefaultAppContainer(private val context: Context) : AppContainer {
    /**
     * App Repository
     * This property is the app repository.
     * The app repository is responsible for providing an interface to the data.
     * @return the app repository
     */
    override val appRepository: AppRepository by lazy {
        Log.i("DefaultAppContainer", "Initializing app repository")
        AppRepositoryImpl(
            AppRemoteApis().getNycOpenDataApi(),
            AppPreferences(context).getSharedPreferences(),
            LocalDatabase.getDatabase(context).jobPostDao()
        )
    }
}
