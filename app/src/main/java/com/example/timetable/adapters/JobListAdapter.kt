package com.example.timetable.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.Job
import com.example.timetable.activities.UpdateActivity
import com.example.timetable.databinding.JobNewBinding
import com.example.timetable.viewmodels.JobViewModel

class JobListAdapter(private val context: Context, private val viewModel: JobViewModel): RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    private var jobList = emptyList<Job>()

    inner class JobViewHolder(private val binding: JobNewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            with(binding) {
                jobStartTimeText.text = jobList[adapterPosition].startTime
                jobEndTimeText.text = jobList[adapterPosition].endTime
                jobNameText.text = jobList[adapterPosition].jobName
                jobTeacherText.text = jobList[adapterPosition].jobTeacher
                jobClassText.text = jobList[adapterPosition].classroom
                jobType.text = jobList[adapterPosition].jobType
                jobCard.setOnClickListener {
                    val intent = Intent(context, UpdateActivity::class.java)
                    intent.putExtra("selected_item", jobList[adapterPosition])
                        .putExtra("databaseName", viewModel.getDatabaseNameObservable().value)
                    startActivity(context, intent, null)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val jobListBinding = JobNewBinding.inflate(layoutInflater, parent, false)
        return JobViewHolder(jobListBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = jobList.size

    fun setData(data : List<Job>){
        this.jobList = data
        notifyDataSetChanged()
    }
}

