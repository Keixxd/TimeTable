package com.example.timetable

import androidx.lifecycle.LiveData

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
}