package com.example.timetable

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update

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
}