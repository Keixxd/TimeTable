package com.example.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobViewModel(application: Application): AndroidViewModel(application) {

    private val jobDao = JobDatabase.getDatabase(application).jobDao()
    private val jobRepository: JobRepository = JobRepository(jobDao)

    fun getDayData(dayName: String?): LiveData<List<Job>>{
        return jobRepository.getDayJobs(dayName)
    }

    fun addJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            jobRepository.addJob(job)
        }
    }

    fun updateJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            jobRepository.updateJob(job)
        }
    }

    fun deleteJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            jobRepository.deleteJob(job)
        }
    }
}