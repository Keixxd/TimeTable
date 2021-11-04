package com.example.timetable.activities

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
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

        setContainersData()
        initTimePickers(binding.startTimePickerCard, binding.startTimeText, startTimeList)
        initTimePickers(binding.endTimePickerCard, binding.endTimeText, endTimeList)

    }

    private fun insertJobToDatabase() {
        with(binding) {
            if(isInputValid(
                    addJobName.text.toString(),
                    addJobTeacher.text.toString(),
                    addJobClass.text.toString()
                )){
                viewModel.updateJob(Job(
                    selectedItem.uid,
                    dayMap[dayPicker.selectedTabPosition],
                    addJobName.text.toString(),
                    addJobTeacher.text.toString(),
                    startTimeText.text.toString(),
                    endTimeText.text.toString(),
                    addJobClass.text.toString(),
                    itemsList[binding.jobTypePicker.selectedTabPosition]
                ))
            }else{
                showErrorDrawables()
                Toast.makeText(this@UpdateActivity, "Заполните пустые поля", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showErrorDrawables(){
        with(binding) {

            val checkState: (EditText) -> Unit = { et ->
                if(et.text.isBlank())
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24, 0)
                else
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0, 0)
            }

            checkState(addJobName)
            checkState(addJobTeacher)
            checkState(addJobClass)
        }
    }

    private fun isInputValid(jobName: String,
                             jobTeacher: String,
                             jobClass: String
    ): Boolean = !(jobName.isBlank() || jobTeacher.isBlank() || jobClass.isBlank())

    private fun deleteJob(){
        viewModel.deleteJob(selectedItem)
        onBackPressed()
    }

    private fun setContainersData() {
        with(binding)
        {
            addJobName.setText(selectedItem.jobName)
            addJobTeacher.setText(selectedItem.jobTeacher)
            addJobClass.setText(selectedItem.classroom)
            dayPicker.getTabAt(
                dayMap.values.toList().indexOf(selectedItem.dayName)
            )!!.select()
            startTimeText.setText(selectedItem.startTime)
            endTimeText.setText(selectedItem.endTime)
            jobTypePicker.getTabAt(itemsList.indexOf(selectedItem.jobType))!!.select()

            dividerLine2.visibility = View.GONE
            groupTitle2.visibility = View.GONE
            batchModeLayout.visibility = View.GONE
        }
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