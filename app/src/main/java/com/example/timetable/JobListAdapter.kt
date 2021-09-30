package com.example.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.databinding.JobBinding

class JobListAdapter(): RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    private var jobList = emptyList<Job>()
    private val EMPTY_LIST = 0
    private val LIST_WITH_ELEMENTS = 1

    inner class JobViewHolder(private val binding: JobBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(){
                binding.jobName.text = jobList[adapterPosition].jobName
                binding.jobTeacher.text = jobList[adapterPosition].jobTeacher
                binding.jobClass.text = jobList[adapterPosition].classroom
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val jobListBinding = JobBinding.inflate(layoutInflater, parent, false)
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

