import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.nycjobs.R
import androidx.compose.runtime.remember
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
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var showFavorites by remember { mutableStateOf(false) }

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
        bottomBar = {
            // Add the bottom navigation bar here.
            BottomNavigationBar(
                showFavorites = showFavorites,
                onHomeClick = { showFavorites = false },
                onFavoritesClick = { showFavorites = true }
            )
        },
        content = { paddingValues ->
            when (val uiState = viewModel.jobPostingsUIState) {
                is JobPostingsUIState.Loading -> LoadingSpinner()
                is JobPostingsUIState.Success -> {
                    val jobsToShow = if (showFavorites) {
                        uiState.data.filter { viewModel.isFavorite(it.jobId) }
                    } else {
                        uiState.data
                    }
                    JobPostList(
                        jobPostings = jobsToShow,
                        loadMoreData = { viewModel.getJobPostings() },
                        updateScrollPosition = { scrollPosition ->
                            viewModel.setScrollingPosition(
                                scrollPosition
                            )
                        },
                        scrollPosition = viewModel.getScrollPosition(),
                        modifier = modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        navController = navController
                    )
                }

                is JobPostingsUIState.Error -> ToastMessage(stringResource(R.string.data_failed))
                else -> ToastMessage(stringResource(R.string.loaded))
            }
        }
    )
}

@Composable
fun BottomNavigationBar(
    showFavorites: Boolean,
    onHomeClick: () -> Unit,
    onFavoritesClick: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Home, contentDescription = "Home")
            },
            label = { Text("Home") },
            selected = !showFavorites,
            onClick = { onHomeClick() }
        )
        NavigationBarItem(
            icon = {
                Icon(Icons.Filled.Favorite, contentDescription = "Favorites")
            },
            label = { Text("Favorites") },
            selected = showFavorites,
            onClick = { onFavoritesClick() }
        )
    }
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
    navController: NavController,
    modifier: Modifier
) {
    val firstVisibleIndex = if (scrollPosition > jobPostings.size) 0 else scrollPosition
    val listState: LazyListState = rememberLazyListState(firstVisibleIndex)

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        items(jobPostings) { jobPost: JobPost ->
            JobsCard(jobPost = jobPost, navController = navController )

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
fun JobsCard(jobPost: JobPost, modifier: Modifier = Modifier, navController: NavController) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable{
                navController.navigate("detail_screen/${jobPost.jobId}")
            },
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






