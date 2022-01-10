package com.example.timetable.presentation.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetable.presentation.adapters.JobListAdapter
import com.example.timetable.databinding.MainListFragmentBinding
import com.example.timetable.presentation.viewmodels.JobViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment(private val dayName: String?, private val viewModel: JobViewModel): Fragment() {

    private lateinit var binding: MainListFragmentBinding

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

        val adapter = JobListAdapter(requireContext(), viewModel)
        binding.jobsList.apply {
            layoutManager = LinearLayoutManager(context)
            binding.jobsList.adapter = adapter
        }

        viewModel.apply {
            getDatabaseNameObservable().observe(viewLifecycleOwner, {tableName ->
                Log.d("info_log", tableName as String)
                Log.d("info_log", dayName as String)
                getDayData(dayName).observe(viewLifecycleOwner, { dayData ->
                    adapter.setData(dayData)

                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                        when(adapter.itemCount){
                            0 -> setListShown(false)
                            else -> setListShown(true)
                        }
                    }
                })
            })
        }
    }

    private fun setListShown(flag: Boolean){
        with(binding){
            if(flag) {
                textView.visibility = View.GONE
                jobsList.visibility = View.VISIBLE
            }else{
                textView.visibility = View.VISIBLE
                jobsList.visibility = View.GONE
            }
        }
    }
}