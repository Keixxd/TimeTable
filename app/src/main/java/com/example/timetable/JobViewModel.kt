package com.example.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobViewModel(application: Application): AndroidViewModel(application) {

    private val jobDao = JobDatabase.getDatabase(application).jobDao()
    private val jobRepository: JobRepository = JobRepository(jobDao)
    val readAllData: LiveData<List<Job>> = jobRepository.readAllData

    fun addJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            jobRepository.addJob(job)
        }
    }
}