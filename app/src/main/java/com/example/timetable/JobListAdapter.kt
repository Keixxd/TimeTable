package com.example.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.databinding.JobBinding

class JobListAdapter: RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    private var jobList = emptyList<Job>()

    inner class JobViewHolder(private val binding: JobBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){
            binding.jobName.text = jobList[adapterPosition].jobName
            binding.jobTeacher.text = jobList[adapterPosition].jobTeacher
            binding.jobClass.text = jobList[adapterPosition].classroom
            binding.jobTime.text = jobList[adapterPosition].time
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val jobListBinding = JobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
