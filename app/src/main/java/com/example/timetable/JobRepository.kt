package com.example.timetable

import androidx.lifecycle.LiveData

class JobRepository(private val jobDao: JobDao) {

    val readAllData: LiveData<List<Job>> = jobDao.loadById()

    suspend fun addJob(job: Job){
        jobDao.addJob(job)
    }
}