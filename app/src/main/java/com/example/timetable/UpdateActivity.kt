package com.example.timetable

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import com.example.timetable.databinding.AddEditActivityBinding
import java.text.SimpleDateFormat
import java.util.*

class UpdateActivity: AppCompatActivity(){

    private lateinit var binding: AddEditActivityBinding
    private val time = Calendar.getInstance()
    private lateinit var itemsList: Array<out String>
    private lateinit var abbrWeekDaysList: Array<out String>
    private lateinit var selectedItem: Job
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setActivityTheme()

        binding = AddEditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedItem = intent.getSerializableExtra("selected_item") as Job
        viewModel.getDatabaseNameObservable().value = intent.getStringExtra("databaseName")
        val startTimeList = selectedItem.startTime?.split(":")
        val endTimeList = selectedItem.endTime?.split(":")

        binding.toolbar2.title = "Изменить"
        setSupportActionBar(binding.toolbar2)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        itemsList = resources.getStringArray(R.array.job_types)
        abbrWeekDaysList = resources.getStringArray(R.array.abbr_week_days)

        initJobTypePicker()
        initDayPicker()

        binding.startTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
        binding.endTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)

        setItemInfo()
        initTimePickers(binding.startTimePickerCard, binding.startTimeText, startTimeList)
        initTimePickers(binding.endTimePickerCard, binding.endTimeText, endTimeList)

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
            val job = Job(selectedItem.uid, dayOfWeek, jobName, jobTeacher, jobStartTime, jobEndTime, jobClass, jobType)
            viewModel.updateJob(job)
            onBackPressed()
        }else{
            Toast.makeText(this, "Заполните пустые поля", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteJob(){
        viewModel.deleteJob(selectedItem)
        onBackPressed()
    }

    private fun isInputValid(jobName: String, jobTeacher: String,
                             jobStartTime: String, jobEndTime: String, jobClass: String): Boolean {

        return !(jobName.isEmpty() && jobTeacher.isEmpty()
                && jobStartTime.isEmpty() && jobClass.isEmpty() && jobEndTime.isEmpty() )
    }

    private fun setItemInfo() {
        binding.addJobName.setText(selectedItem.jobName)
        binding.addJobTeacher.setText(selectedItem.jobTeacher)
        binding.addJobClass.setText(selectedItem.classroom)
        binding.dayPicker.getTabAt(dayMap.values.toList().binarySearch(selectedItem.dayName))!!.select()
        binding.startTimeText.setText(selectedItem.startTime)
        binding.endTimeText.setText(selectedItem.endTime)
        binding.jobTypePicker.getTabAt(itemsList.binarySearch(selectedItem.jobType))!!.select()

        binding.dividerLine2.visibility = View.GONE
        binding.groupTitle2.visibility = View.GONE
        binding.batchModeLayout.visibility = View.GONE
    }

    private fun initTimePickers(pickerCard: CardView, timeTextView: TextView, timeList: List<String>?){
        pickerCard.setOnClickListener {
            time.set(Calendar.HOUR_OF_DAY, timeList?.get(0)!!.toInt())
            time.set(Calendar.MINUTE, timeList.get(1).toInt())
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                time.set(Calendar.HOUR_OF_DAY, hourOfDay)
                time.set(Calendar.MINUTE, minute)
                timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
            }
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show()
        }
    }

    private fun initDayPicker(){
        for(i in abbrWeekDaysList)
            binding.dayPicker.addTab(binding.dayPicker.newTab().setText(i))
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
        R.id.delete_job ->{
            deleteJob()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.update_activity_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}