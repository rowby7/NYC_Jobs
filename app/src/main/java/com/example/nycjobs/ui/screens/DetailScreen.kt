package com.example.nycjobs.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.nycjobs.model.JobPost
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.ui.unit.dp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(jobPost: JobPost,
                 viewModel: JobPostingsViewModel,
                 navController: NavController){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = jobPost.agency)},
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
//                    val isFavorite =viewModel.isFavorite(jobPost.jobId)
                    IconButton(onClick = { viewModel.toggleFavorite(jobPost.jobId)}){
                        Icon(Icons.Filled.FavoriteBorder, contentDescription = "Favorite Button")
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Job Id: ${jobPost.jobId}")
                Text(text = "Posting Date: ${jobPost.postingDate}")
            }
            Column(modifier = Modifier

            ){
                Text(text = "Business Title: ")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "${jobPost.businessTitle}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Career Level:  ${jobPost.careerLevel}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Salary Range:  ${jobPost.salaryRangeFrom} - ${jobPost.salaryRangeTo} Annual")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Job Catergory: ")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "${jobPost.jobCategory}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Work Location: ")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "${jobPost.workLocation}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Division: ")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "${jobPost.divisionWorkUnit}")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Job Description: ")

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "${jobPost.jobDescription}")


            }
        }

    }

}


