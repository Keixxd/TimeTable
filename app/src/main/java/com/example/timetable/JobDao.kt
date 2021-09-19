package com.example.timetable

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface JobDao {
    @Query("SELECT * FROM job_table")
    fun loadById(): LiveData<List<Job>>

    @Insert
    suspend fun addJob(job: Job)
}