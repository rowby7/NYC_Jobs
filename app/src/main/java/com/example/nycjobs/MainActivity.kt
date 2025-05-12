package com.example.nycjobs

import HomeScreen
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier


import com.example.nycjobs.ui.screens.JobPostingsViewModel
import com.example.nycjobs.ui.theme.NYCJobsTheme
import com.example.nycjobs.util.TAG

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "MainActivity OnCreate" )
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYCJobsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    //TODO: get view model here and pass it to HomeScreen
                    //Factory is to create instances of JobPostingsViewModel
                    //provides the dependencies of the viewmodel
                    val viewModel: JobPostingsViewModel by viewModels { JobPostingsViewModel.Factory }
                    HomeScreen(viewModel)
                }
            }
        }
    }
}


