package com.example.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timetable.databinding.MainListFragmentBinding

class ListFragment(position: Int): Fragment() {

    private lateinit var binding: MainListFragmentBinding
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainListFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = JobListAdapter()
        binding.jobsList.apply {
            layoutManager = LinearLayoutManager(context)
            binding.jobsList.adapter = adapter
        }

        viewModel.readAllData.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })
    }
}