package com.example.timetable.model

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.example.timetable.Job

@Dao
interface JobDao {
    @Query("SELECT * FROM job_table")
    fun loadAll(): LiveData<List<Job>>

    @Query("SELECT * FROM job_table WHERE day_name = :day_name")
    fun loadByDayName(day_name: String?): LiveData<List<Job>>

    @Insert(onConflict = REPLACE)
    suspend fun addJob(job: Job)

    @Update
    suspend fun updateJob(job: Job)

    @Delete
    suspend fun deleteJob(job: Job)
}