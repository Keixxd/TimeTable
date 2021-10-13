package com.example.timetable

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JobViewModel(application: Application, name: String): AndroidViewModel(application) {

    private val jobDao = JobDatabase.getDatabase(application, name).jobDao()
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

class JobViewModelFactory(application: Application, name: String): ViewModelProvider.Factory{

    private val factoryApplication = application
    private val factoryName = name

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            return JobViewModel(factoryApplication, factoryName) as T
        }catch (e: Exception){
            throw e
        }
    }
}