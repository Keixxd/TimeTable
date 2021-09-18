package com.example.timetable

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timetable.databinding.MainListFragmentBinding

class PagerAdapter(val context: Context): RecyclerView.Adapter<PagerAdapter.PagerViewHolder>(){

    val list = mutableListOf<Int>(1,2,3,4,5)
    val jbAdapter = JobListAdapter()

    inner class PagerViewHolder(private val pagerBinding: MainListFragmentBinding): RecyclerView.ViewHolder(pagerBinding.root){
        fun bind(){
            pagerBinding.jobsList.apply {
                layoutManager = LinearLayoutManager(context)
                pagerBinding.jobsList.adapter = jbAdapter
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val pagerBinding = MainListFragmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PagerViewHolder(pagerBinding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = list.size
}