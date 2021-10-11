package com.example.timetable

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.timetable.databinding.ActivityAddBinding
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

class UpdateActivity: AppCompatActivity(){

    private lateinit var binding: ActivityAddBinding
    private val time = Calendar.getInstance()
    private var dayPicked: String? = null
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addActionBar = supportActionBar
        addActionBar?.setDisplayHomeAsUpEnabled(true)
        val itemsList = resources.getStringArray(R.array.week_days)
        val selectedItem = intent.getSerializableExtra("selected_item") as Job

        dayPicked = selectedItem.dayName
        setItemInfo(selectedItem)

        //временное решение ???????
        val startTimeList = binding.startTimeText.text.split(":")
        val endTimeList = binding.endTimeText.text.split(":")

        initTimePickers(binding.startTimePickerLayout, binding.startTimeText, startTimeList)
        initTimePickers(binding.endTimePickerLayout, binding.endTimeText, endTimeList)

        initDayOfWeekPicker(itemsList)

        binding.addCancel.setOnClickListener {
            onBackPressed()
        }

        binding.addConfirm.setOnClickListener {
            updateJobEvent(selectedItem.uid)
        }

    }

    private fun updateJobEvent(id: Long?) {
        val jobName = binding.addJobName.text.toString()
        val jobTeacher = binding.addJobTeacher.text.toString()
        val jobClass = binding.addJobClass.text.toString()
        val jobStartTime = binding.startTimeText.text.toString()
        val jobEndTime = binding.endTimeText.text.toString()
        val jobIsEven = binding.checkEven.isChecked
        val dayOfWeek = dayPicked

        //val job = Job(id, dayOfWeek, jobName, jobTeacher, jobStartTime, jobEndTime, jobClass, jobIsEven)
        //viewModel.updateJob(job)
        onBackPressed()
    }

    private fun setItemInfo(selectedItem: Job) {
        binding.addJobName.setText(selectedItem.jobName)
        binding.addJobTeacher.setText(selectedItem.jobTeacher)
        binding.addJobClass.setText(selectedItem.classroom)
        binding.dayOfWeekText.setText(selectedItem.dayName)
        binding.startTimeText.setText(selectedItem.startTime)
        binding.endTimeText.setText(selectedItem.endTime)
        //binding.checkEven.isChecked = selectedItem.evenWeek
    }

    private fun initTimePickers(pickerLayout: LinearLayout, timeTextView: TextView, timeList: List<String>){

        pickerLayout.setOnClickListener {
            time.set(Calendar.HOUR_OF_DAY, timeList[0].toInt())
            time.set(Calendar.MINUTE, timeList[1].toInt())
            val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                time.set(Calendar.HOUR_OF_DAY, hourOfDay)
                time.set(Calendar.MINUTE, minute)
                timeTextView.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(time.time)
            }
            TimePickerDialog(this, timeSetListener, time.get(Calendar.HOUR_OF_DAY), time.get(
                Calendar.MINUTE), true).show()
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

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}