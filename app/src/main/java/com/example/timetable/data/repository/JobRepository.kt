package com.example.timetable.data.repository

import androidx.lifecycle.LiveData
import com.example.timetable.Job
import com.example.timetable.data.database.JobDao

class JobRepository(private val jobDao: JobDao) {

    val readAllData: LiveData<List<Job>> = jobDao.loadAll()

    fun getDayJobs(dayName: String?): LiveData<List<Job>>{
        return jobDao.loadByDayName(dayName)
    }

    suspend fun addJob(job: Job){
        jobDao.addJob(job)
    }

    suspend fun updateJob(job: Job){
        jobDao.updateJob(job)
    }

    suspend fun deleteJob(job: Job){
        jobDao.deleteJob(job)
    }

    suspend fun clearTable(){
        jobDao.clearTable()
    }
}