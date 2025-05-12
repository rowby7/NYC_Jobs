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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nycjobs.ui.screens.DetailScreen
import com.example.nycjobs.ui.screens.JobPostingsViewModel
import com.example.nycjobs.util.TAG
import com.example.nycjobs.ui.theme.NYCJobsTheme

import androidx.compose.runtime.*

import com.example.nycjobs.model.JobPost




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG, "MainActivity OnCreate")
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController: NavHostController = rememberNavController()
            val viewModel: JobPostingsViewModel by viewModels { JobPostingsViewModel.Factory }

            NYCJobsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    NavHost(navController = navController, startDestination = "home_screen") {

                        composable("home_screen") {
                            HomeScreen(viewModel, navController)
                        }


                        composable("detail_screen/{jobId}") { backStackEntry ->
                            val jobId = backStackEntry.arguments?.getString("jobId")?.toInt()

                            var jobPost by remember { mutableStateOf<JobPost?>(null) }

                            LaunchedEffect(jobId) {
                                jobId?.let {
                                    viewModel.getJobPost(it) { fetchedJobPost ->
                                        jobPost = fetchedJobPost
                                    }
                                }
                            }

                            jobPost?.let { DetailScreen(it) }
                        }
                    }
                }
            }
        }
    }
}



