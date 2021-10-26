package com.example.timetable.activities

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.timetable.*
import com.example.timetable.databinding.AddEditActivityBinding
import com.example.timetable.viewmodels.JobViewModel
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var binding: AddEditActivityBinding
    private val time = Calendar.getInstance()
    private lateinit var itemsList: Array<out String>
    private lateinit var abbrWeekDaysList: Array<out String>
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityTheme()

        viewModel.getDatabaseNameObservable().value = intent.getStringExtra("databaseName")

        binding = AddEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar2.title = "Добавить"
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemsList = resources.getStringArray(R.array.job_types)
        abbrWeekDaysList = resources.getStringArray(R.array.abbr_week_days)

        initJobTypePicker()
        initDayPicker()

        binding.startTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
        binding.endTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)

        initTimePickers(binding.startTimePickerCard, binding.startTimeText)
        initTimePickers(binding.endTimePickerCard, binding.endTimeText)

    }

    private fun initDayPicker(){
        for(i in abbrWeekDaysList)
            binding.dayPicker.addTab(binding.dayPicker.newTab().setText(i))
    }

    private fun insertJobToDatabase() {
        val jobName = binding.addJobName.text.toString()
        val jobTeacher = binding.addJobTeacher.text.toString()
        val jobClass = binding.addJobClass.text.toString()
        val jobStartTime = binding.startTimeText.text.toString()
        val jobEndTime = binding.endTimeText.text.toString()
        val jobType = itemsList[binding.jobTypePicker.selectedTabPosition]
        val dayOfWeek = dayMap[binding.dayPicker.selectedTabPosition]

        if (isInputValid(jobName, jobTeacher, jobStartTime, jobEndTime, jobClass)) {
            val job = Job(null, dayOfWeek, jobName, jobTeacher, jobStartTime, jobEndTime, jobClass, jobType)
            viewModel.addJob(job)
            onBackPressed()
        }else{
            Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isInputValid(jobName: String, jobTeacher: String,
                             jobStartTime: String, jobEndTime: String, jobClass: String): Boolean {

        return !(jobName.isEmpty() && jobTeacher.isEmpty()
                && jobStartTime.isEmpty() && jobClass.isEmpty() && jobEndTime.isEmpty() )
    }

    private fun initTimePickers(pickerCard: CardView, timeTextView: TextView){

        pickerCard.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                time.set(Calendar.HOUR_OF_DAY, hourOfDay)
                time.set(Calendar.MINUTE, minute)
                timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
            }
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show()
        }
    }

    private fun initJobTypePicker(){
        for (i in itemsList)
            binding.jobTypePicker.addTab(binding.jobTypePicker.newTab().setText(i))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.save_job -> {
            insertJobToDatabase()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_activity_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}