package com.example.timetable

import android.app.TimePickerDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.example.timetable.databinding.ActivityAddBinding
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private val viewModel by viewModels<JobViewModel>()
    private lateinit var binding: ActivityAddBinding
    private val time = Calendar.getInstance()
    private var dayPicked = "Понедельник"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addActionBar = supportActionBar
        addActionBar?.setDisplayHomeAsUpEnabled(true)
        val itemsList = resources.getStringArray(R.array.week_days)

        binding.dayOfWeekText.text = dayPicked
        binding.startTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
        binding.endTimeText.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)

        initTimePickers(binding.startTimePickerLayout, binding.startTimeText)
        initTimePickers(binding.endTimePickerLayout, binding.endTimeText)

        initDayOfWeekPicker(itemsList)

        binding.addConfirm.setOnClickListener{
            insertJobToDatabase()
        }

        binding.addCancel.setOnClickListener {
            onBackPressed()
        }

        binding.evenWeekCheckerLayout.setOnClickListener {
            setCheckBoxChecked()
        }
    }

    private fun insertJobToDatabase() {
        val jobName = binding.addJobName.text.toString()
        val jobTeacher = binding.addJobTeacher.text.toString()
        val jobClass = binding.addJobClass.text.toString()
        val jobStartTime = binding.startTimeText.text.toString()
        val jobEndTime = binding.endTimeText.text.toString()
        val jobIsEven = binding.checkEven.isChecked
        val dayOfWeek = dayPicked

        if (isInputValid(jobName, jobTeacher, jobStartTime, jobEndTime, jobClass)) {
            val job = Job(null, dayOfWeek, jobName, jobTeacher, jobStartTime, jobEndTime, jobClass, jobIsEven)
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

    private fun initTimePickers(pickerLayout: LinearLayout, timeTextView: TextView){

        pickerLayout.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                time.set(Calendar.HOUR_OF_DAY, hourOfDay)
                time.set(Calendar.MINUTE, minute)
                timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
            }
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(Calendar.MINUTE), true).show()
        }
    }

    private fun initDayOfWeekPicker(itemsList: Array<out String>){
        val picker = AlertDialog.Builder(this)
        picker.setTitle(R.string.day_picker_dialog_title)
            .setItems(itemsList, object: DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dayPicked = itemsList[which]
                    binding.dayOfWeekText.text = dayPicked
                }
            })
        picker.create()
        binding.dayOfWeekPickerLayout.setOnClickListener {
            picker.show()
        }
    }

    private fun setCheckBoxChecked(){
        if(binding.checkEven.isChecked)
            binding.checkEven.isChecked = false
        else
            binding.checkEven.isChecked = true
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}