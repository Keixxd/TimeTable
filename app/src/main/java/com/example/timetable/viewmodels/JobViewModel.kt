package com.example.timetable.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.example.timetable.Job
import com.example.timetable.model.JobDatabase
import com.example.timetable.model.JobRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Serializable

class JobViewModel(application: Application): AndroidViewModel(application), Serializable {

    private val databaseName = MutableLiveData<String?>()
    private val viewModelApplication = application

    fun getDatabaseNameObservable():MutableLiveData<String?> = databaseName

    fun getDayData(dayName: String?): LiveData<List<Job>>{
        return JobRepository(
            JobDatabase.getDatabase(viewModelApplication, databaseName.value as String)
            .jobDao()).getDayJobs(dayName)
    }

    fun addJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            JobRepository(
                JobDatabase.getDatabase(viewModelApplication, databaseName.value as String)
                .jobDao()).addJob(job)
        }
    }

    fun updateJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            JobRepository(
                JobDatabase.getDatabase(viewModelApplication, databaseName.value as String)
                .jobDao()).updateJob(job)
        }
    }

    fun deleteJob(job: Job){
        viewModelScope.launch(Dispatchers.IO) {
            JobRepository(
                JobDatabase.getDatabase(viewModelApplication, databaseName.value as String)
                .jobDao()).deleteJob(job)
        }
    }

    fun clearTable(){
        viewModelScope.launch(Dispatchers.IO) {
            JobRepository(
                JobDatabase.getDatabase(viewModelApplication, databaseName.value as String)
                    .jobDao()).clearTable()
        }
    }
}