package com.example.timetable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.viewModels
import com.example.timetable.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {

    private val viewModel by viewModels<JobViewModel>()
    private lateinit var binding: ActivityAddBinding
    private lateinit var selectedItem: Any

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val spinnerList = resources.getStringArray(R.array.week_days)
        initSpinner(spinnerList)
        binding.button.setOnClickListener{
            insertJobToDatabase(spinnerList)
        }
    }

    private fun initSpinner(spinnerList: Array<String>) {
        if(binding.spinner.isNull()){
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList)
            binding.spinner.adapter = adapter
        }

        binding.spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedItem = parent!!.getItemAtPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //something here
            }
        }
    }

    private fun insertJobToDatabase(spinnerList: Array<String>) {
        val jobName = binding.addJobName.text.toString()
        val jobTeacher = binding.addJobTeacher.text.toString()
        val jobTime = binding.addJobTime.text.toString()
        val jobClass = binding.addJobClass.text.toString()
        val isWeekEven = binding.checkForEven.isChecked

        if(isInputValid(jobName, jobTeacher, jobTime, jobClass)){
            val job = Job(0,selectedItem.toString(), jobName, jobTeacher, jobTime, jobClass, isWeekEven)
            viewModel.addJob(job)
            onBackPressed()
        }
    }

    private fun isInputValid(jobName: String, jobTeacher: String, jobTime: String, jobClass: String): Boolean {
        return !(jobName.isEmpty() && jobTeacher.isEmpty() && jobTime.isEmpty() && jobClass.isEmpty())
    }

    fun Spinner.isNull(): Boolean{
        return this != null
    }
}