package com.example.nycjobs.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.example.nycjobs.model.JobPost

/**
 * Job Post Data Access Object (DAO).
 * Defines data retrieval methods for the JobPost entity.
 */
@Dao
interface JobPostDao {
    @Query("SELECT * FROM JobPost ORDER BY postingLastUpdated DESC")
    fun getAll(): Flow<List<JobPost>>

    @Query("SELECT * FROM JobPost WHERE jobId = :id")
    fun get(id: Int): Flow<JobPost>

    @Upsert
    suspend fun upsert(jobPostings: List<JobPost>)
}

/**
 * Local Database definition.
 * Handles database operations for JobPost entities.
 */
@Database(entities = [JobPost::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun jobPostDao(): JobPostDao

    companion object {
        private const val DATABASE = "local_database"

        @Volatile
        private var Instance: LocalDatabase? = null

        /** Provides database instance */
        fun getDatabase(context: Context): LocalDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, LocalDatabase::class.java, DATABASE)
                    .fallbackToDestructiveMigration()
                    .build().also { Instance = it }
            }
        }
    }
}
