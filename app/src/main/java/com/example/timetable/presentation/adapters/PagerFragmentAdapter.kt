package com.example.timetable.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timetable.presentation.ui.ListFragment
import com.example.timetable.NUM_PAGES
import com.example.timetable.dayMap
import com.example.timetable.presentation.viewmodels.JobViewModel


class PagerFragmentAdapter(fa: FragmentActivity, private val viewModel: JobViewModel) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {

        return ListFragment(dayMap.get(position), viewModel)
    }
}