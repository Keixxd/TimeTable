package com.example.timetable

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerFragmentAdapter(fa: FragmentActivity, private val viewModel: JobViewModel) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {

        return ListFragment(dayMap.get(position), viewModel)
    }
}