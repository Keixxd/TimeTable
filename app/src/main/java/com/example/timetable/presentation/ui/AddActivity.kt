package com.example.timetable.presentation.ui

import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.timetable.*
import com.example.timetable.databinding.AddEditActivityBinding
import com.example.timetable.presentation.viewmodels.JobViewModel
import com.example.timetable.utils.setActivityTheme
import java.text.SimpleDateFormat
import java.util.*

class AddActivity : AppCompatActivity() {

    private lateinit var binding: AddEditActivityBinding
    private val time = Calendar.getInstance()
    private lateinit var itemsList: Array<String>
    private lateinit var abbrWeekDaysList: Array<String>
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
        with(binding) {
            if(isInputValid(
                addJobName.text.toString(),
                addJobTeacher.text.toString(),
                addJobClass.text.toString()
            )){
                viewModel.addJob(Job(
                    null,
                    dayMap[dayPicker.selectedTabPosition],
                    addJobName.text.toString(),
                    addJobTeacher.text.toString(),
                    startTimeText.text.toString(),
                    endTimeText.text.toString(),
                    addJobClass.text.toString(),
                    itemsList[binding.jobTypePicker.selectedTabPosition]
                ))
                // batch mode check
                if(!batchSwitch.isChecked){
                    onBackPressed()
                    finish()
                }
            }else{
                showErrorDrawables()
                Toast.makeText(this@AddActivity, "Заполните пустые поля", Toast.LENGTH_LONG).show()
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

    override fun onDestroy() {
        super.onDestroy()
        Log.d("info_log", "onDestroy")
    }
}