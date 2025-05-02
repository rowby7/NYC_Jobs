package com.example.nycjobs.data

import android.content.SharedPreferences
import android.util.Log
import com.example.nycjobs.api.NycOpenDataService
import com.example.nycjobs.model.JobPost
import kotlinx.coroutines.flow.first

/**
 * App repository interface.
 * Responsible for providing data to the ViewModel.
 */
interface AppRepository {
    fun getScrollPosition(): Int
    fun setScrollPosition(position: Int)
    suspend fun getJobPostings(): List<JobPost>
    suspend fun getJobPost(jobId: Int): JobPost
}

/**
 * Implementation of AppRepository providing data to the ViewModel.
 *
 * @param nycOpenDataAPI API service for fetching job postings.
 * @param sharedPreferences Shared preferences for storing user data.
 * @param dao Data access object for fetching job posts from the local database.
 */
class AppRepositoryImpl(
    private val nycOpenDataAPI: NycOpenDataService,
    private val sharedPreferences: SharedPreferences,
    private val dao: JobPostDao
) : AppRepository {

    private val scrollPositionKey = "scroll_position"
    private val offsetKey = "offset"
    private var offset = sharedPreferences.getInt(offsetKey, 0)
    private var totalJobs = 0

    /** Updates the offset used for pagination. */
    private fun updateOffset() {
        offset += (totalJobs - offset)
        sharedPreferences.edit().putInt(offsetKey, offset).apply()
    }

    /** Updates the total job count. */
    private fun updateTotalJobs(newTotalJobs: Int) {
        totalJobs = newTotalJobs
    }

    /**
     * Fetches job postings from either the local database or the NYC Open Data API.
     *
     * If totalJobs is 0, the API is queried. Otherwise, local data is returned.
     *
     * @return List of job postings.
     */
    override suspend fun getJobPostings(): List<JobPost> {
        updateOffset()
        val localData = dao.getAll().first()
        updateTotalJobs(localData.size)

        if (offset == totalJobs) {
            val jobs = nycOpenDataAPI.getJobPostings(limit = 100, offset = offset)
            dao.upsert(jobs)
            val updatedData = dao.getAll().first()
            updateTotalJobs(updatedData.size)
            return updatedData
        }
        return localData
    }

    /** Retrieves a job post from the local database. */
    override suspend fun getJobPost(jobId: Int): JobPost {
        return dao.get(jobId).first()
    }

    /** Gets the scroll position from shared preferences. */
    override fun getScrollPosition(): Int {
        return sharedPreferences.getInt(scrollPositionKey, 0)
    }

    /** Saves scroll position in shared preferences. */
    override fun setScrollPosition(position: Int) {
        sharedPreferences.edit().putInt(scrollPositionKey, position).apply()
    }
}
