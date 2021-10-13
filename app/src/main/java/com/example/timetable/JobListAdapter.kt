package com.example.timetable

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.databinding.JobNewBinding

class JobListAdapter(val context: Context): RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    private var jobList = emptyList<Job>()
    private val EMPTY_LIST = 0
    private val LIST_WITH_ELEMENTS = 1

    inner class JobViewHolder(private val binding: JobNewBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.jobStartTimeText.text = jobList[adapterPosition].startTime
            binding.jobEndTimeText.text = jobList[adapterPosition].endTime
            binding.jobNameText.text = jobList[adapterPosition].jobName
            binding.jobTeacherText.text = jobList[adapterPosition].jobTeacher
            binding.jobClassText.text = jobList[adapterPosition].classroom
            binding.jobType.text = jobList[adapterPosition].jobType
            binding.jobCard.setOnClickListener{
                val intent = Intent(context, UpdateActivity::class.java)
                intent.putExtra("selected_item", jobList[adapterPosition])
                startActivity(context, intent, null)
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

    private fun getViewType(): Int{
        when(itemCount){
            0 -> return EMPTY_LIST
            else -> return LIST_WITH_ELEMENTS
        }
    }
}

