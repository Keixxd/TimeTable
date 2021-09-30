package com.example.timetable

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
import java.time.DayOfWeek
import java.util.*
import kotlin.collections.ArrayList

class AddActivity : AppCompatActivity() {

    private val viewModel by viewModels<JobViewModel>()
    private lateinit var binding: ActivityAddBinding
    private val time = Calendar.getInstance()
    private var dayPicked = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemsList = resources.getStringArray(R.array.week_days)

        val currentTime = DateUtils.formatDateTime(this,time.timeInMillis,
            DateUtils.FORMAT_SHOW_TIME)
        val currentTimeList = currentTime.split(":")
        binding.dayOfWeekText.text = resources.getText(R.string.day_of_week_picker_default)
        binding.startTimeText.text = currentTime
        binding.endTimeText.text = currentTime

        initTimePickers(binding.startTimePicker, currentTimeList)
        initTimePickers(binding.endTimePicker, currentTimeList)

        initDayOfWeekPicker(itemsList)

        binding.addConfirm.setOnClickListener{
            insertJobToDatabase()
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
        }
    }

    private fun isInputValid(jobName: String, jobTeacher: String,
                             jobStartTime: String, jobEndTime: String, jobClass: String): Boolean {

        return !(jobName.isEmpty() && jobTeacher.isEmpty()
                && jobStartTime.isEmpty() && jobClass.isEmpty() && jobEndTime.isEmpty() )
    }

    private fun initTimePickers(pickerLayout: LinearLayout, timeList: List<String>){

        pickerLayout.setOnClickListener{
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(timeList[0].toInt())
                .setMinute(timeList[1].toInt())
                .setTitleText(R.string.timepicker_title)
                .build()
            timePicker.addOnPositiveButtonClickListener{

                time.set(Calendar.HOUR_OF_DAY, timePicker.hour)
                time.set(Calendar.MINUTE, timePicker.minute)

                when(pickerLayout.id) {
                    R.id.startTimePicker -> setViewTime(binding.startTimeText, timePicker)
                    R.id.endTimePicker -> setViewTime(binding.endTimeText, timePicker)
                }
            }
            timePicker.addOnCancelListener {
                timePicker.dismiss()
            }
            timePicker.show(supportFragmentManager, "time_picker")
        }
    }

    private fun setViewTime(timeTextView: TextView, timePicker: MaterialTimePicker) {
        if(timePicker.minute < 10)
            timeTextView.text = "${timePicker.hour}:0${timePicker.minute}"
        else
            timeTextView.text = "${timePicker.hour}:${timePicker.minute}"
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
        binding.dayOfWeekPicker.setOnClickListener {
            picker.show()
        }
    }
}