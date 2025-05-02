package com.example.nycjobs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize


import androidx.compose.material3.Surface

import androidx.compose.ui.Modifier

import com.example.nycjobs.ui.screens.HomeScreen
import com.example.nycjobs.ui.theme.NYCJobsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NYCJobsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ){
                    //TODO: get view model here and pass it to HomeScreen
                    HomeScreen(modifier = Modifier)
                }
            }
        }
    }
}


