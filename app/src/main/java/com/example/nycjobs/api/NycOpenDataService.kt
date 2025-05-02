package com.example.nycjobs.api

import android.util.Log
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.util.TAG
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/*
Retro fit interface for the nyc open data api

this interface defines the api endpoints for the nyc open data api
 */
interface NycOpenDataService {
    @GET("kpav-sd4t.json?posting_type=External&\$order=posting_updated%20DESC")
    suspend fun getJobPostings(
        @Query("\$limit") limit: Int = 50,
        @Query("\$offset") offset: Int
    ):
            List<JobPost>
}
/*
App remote apis

this class defines the remote apis for the app
 */
 class AppRemoteApis {

     private val baseUrl = "https://data.cityofnewyork.us/resource/"
     private val contentType = MediaType.get("application/json; charset=utf-8")
     private val json = Json {
         ignoreUnknownKeys = true

     }

     /*
     Get the API calls


     @return the interface that defines the API endpoints
      */

     fun getNycOpenDataApi(): NycOpenDataService {
         Log.i(TAG, "retrofit service creating API calls")
         return Retrofit.Builder()
             .baseUrl(baseUrl)
             .addConverterFactory(json.asConverterFactory(contentType))
             .build()
             .create(NycOpenDataService::class.java)
     }
 }


