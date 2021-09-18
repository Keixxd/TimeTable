package com.example.timetable

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timetable.databinding.JobBinding

class JobListAdapter: RecyclerView.Adapter<JobListAdapter.JobViewHolder>() {

    val list = mutableListOf<Int>(1,4,6,2,6,8,2,6,3)

    inner class JobViewHolder(private val binding: JobBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(data: MutableList<Int>){
            binding.number.text = data[adapterPosition].toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val jobListBinding = JobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return JobViewHolder(jobListBinding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        holder.bind(list)
    }

    override fun getItemCount(): Int = list.size

}
