import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.nycjobs.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import com.example.nycjobs.model.JobPost
import com.example.nycjobs.ui.screens.JobPostingsUIState
import com.example.nycjobs.ui.screens.LoadingSpinner
import com.example.nycjobs.ui.screens.ToastMessage
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Search
import com.example.nycjobs.ui.screens.JobPostingsViewModel

/**  Home Screen
 *   This composable function is the home screen for the app.
 *   It's responsible for displaying the list of job postings.
 *   @param viewModel the view model for the app
 *   @param modifier the modifier for the composable
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: JobPostingsViewModel,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                navigationIcon = {
                    IconButton(onClick = { println("Search button clicked (not yet implemented)") }){
                        Icon(Icons.Filled.Search, contentDescription = "Search")
                    }
                }

            )
        },
        content = { paddingValues ->
            when (val uiState = viewModel.jobPostingsUIState) {
                is JobPostingsUIState.Loading -> LoadingSpinner()
                is JobPostingsUIState.Success -> {
                    JobPostList(
                        jobPostings = uiState.data,
                        loadMoreData = { viewModel.getJobPostings() },
                        updateScrollPosition = { scrollPosition ->
                            viewModel.setScrollingPosition(
                                scrollPosition
                            )
                        },
                        scrollPosition = viewModel.getScrollPosition(),
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues) // Apply padding to JobList
                    )
                }

                is JobPostingsUIState.Error -> ToastMessage(stringResource(R.string.data_failed))
                else -> ToastMessage(stringResource(R.string.loaded))
            }
        }
    )
}

/**  Job Post List
 *   This composable function is the list of job postings.
 *   @param jobPostings the list of job postings
 *   @param loadMoreData the function to load more data
 *   @param updateScrollPosition the function to update the scroll position
 *   @param scrollPosition the current scroll position
 *   @param modifier the modifier for the composable
 */
@OptIn(FlowPreview::class)
@Composable

fun JobPostList(
    jobPostings: List<JobPost>,
    loadMoreData: () -> Unit,
    updateScrollPosition: (Int) -> Unit,
    scrollPosition: Int,
    modifier: Modifier
) {
    val firstVisibleIndex = if (scrollPosition > jobPostings.size) 0 else scrollPosition
    val listState: LazyListState = rememberLazyListState(firstVisibleIndex)

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(jobPostings) { jobPost: JobPost ->
            JobsCard(jobPost = jobPost)

        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .debounce(timeoutMillis = 500L)
            .collect { lastVisibleItemIndex ->
                updateScrollPosition(listState.firstVisibleItemIndex)
                if (lastVisibleItemIndex != null && lastVisibleItemIndex >= jobPostings.size - 1) {
                    loadMoreData()
                }
            }
    }
}


@Composable
fun JobsCard(jobPost: JobPost, modifier: Modifier = Modifier) {

    Card(
        modifier = Modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = jobPost.careerLevel,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Arrow",
                modifier = Modifier.size(8.dp)
            )
        }
            Text(
                text = jobPost.agency,
                style = MaterialTheme.typography.headlineMedium
                )
            Text(
                text = jobPost.civilServiceTitle,
                style = MaterialTheme.typography.bodyMedium
                )
        }

    }
}



@Preview(showBackground = false)
@Composable
fun PreviewJobsCard() {
    val dummyJobPost = JobPost(
        jobId = 1234,
        agency = "Department of Technology",
        numOfOpenPositions = "3",
        businessTitle = "Software Engineer",
        civilServiceTitle = "Senior Developer",
        jobCategory = "IT & Software",
        fullOrPartTime = 'F',
        careerLevel = "Mid-Level",
        salaryRangeFrom = 85000.0,
        salaryRangeTo = 120000.0,
        salaryFrequency = "Annual",
        agencyLocation = "New York",
        divisionWorkUnit = "Software Division",
        jobDescription = "Develop and maintain software applications.",
        minRequirement = "Bachelor's degree in Computer Science.",
        preferredSkills = "Experience with Kotlin and Jetpack Compose.",
        additionalInfo = "Flexible work schedule.",
        toApply = "Submit resume online.",
        workLocation = "New York Office",
        postingDate = "2025-04-10",
        postUntil = "2025-06-10",
        postingLastUpdated = "2025-05-01"
    )

    JobsCard(jobPost = dummyJobPost)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun JobPostListPreview() {
    val sampleJobs = listOf(
        JobPost(
            jobId = 1,
            agency = "Parks Department",
            numOfOpenPositions = "5",
            businessTitle = "Horticulturist",
            civilServiceTitle = "Gardener",
            jobCategory = "Environmental",
            fullOrPartTime = 'F',
            careerLevel = "Entry-Level",
            salaryRangeFrom = 45000.0,
            salaryRangeTo = 60000.0,
            salaryFrequency = "Annual",
            agencyLocation = "Central Park",
            divisionWorkUnit = "Horticulture Division",
            jobDescription = "Planting and maintaining gardens...",
            postingDate = "2024-05-03",
            postingLastUpdated = "2024-05-03"
        ),
        JobPost(
            jobId = 2,
            agency = "Department of Technology",
            numOfOpenPositions = "2",
            businessTitle = "Software Engineer",
            civilServiceTitle = "Computer Programmer",
            jobCategory = "Technology",
            fullOrPartTime = 'F',
            careerLevel = "Mid-Level",
            salaryRangeFrom = 80000.0,
            salaryRangeTo = 110000.0,
            salaryFrequency = "Annual",
            agencyLocation = "Downtown Brooklyn",
            divisionWorkUnit = "Software Development Team",
            jobDescription = "Developing and maintaining applications...",
            postingDate = "2024-05-01",
            postingLastUpdated = "2024-05-01"
        ),
        // Add more sample JobPost objects
    )

    JobPostList(
        jobPostings = sampleJobs,
        loadMoreData = {
            // Dummy implementation for preview
            println("Load more data triggered in preview")
        },
        updateScrollPosition = { position ->
            // Dummy implementation for preview
            println("Scroll position updated to $position in preview")
        },
        scrollPosition = 0, // Initial scroll position for preview
        modifier = Modifier.fillMaxSize() // Make the list take up the preview area
    )
}


