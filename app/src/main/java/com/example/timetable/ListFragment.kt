package com.example.timetable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timetable.databinding.MainListFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ListFragment(val dayName: String?): Fragment() {

    private lateinit var binding: MainListFragmentBinding
    private val viewModel:
            JobViewModel by viewModels { JobViewModelFactory(requireActivity().application, "job_database")}

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

        val adapter = JobListAdapter(requireContext())
        binding.jobsList.apply {
            layoutManager = LinearLayoutManager(context)
            binding.jobsList.adapter = adapter
        }

        viewModel.getDayData(dayName).observe(viewLifecycleOwner, {dayData ->
            adapter.setData(dayData)

            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main){
                if(adapter.itemCount > 0){
                    binding.textView.visibility = View.GONE
                    binding.jobsList.visibility = View.VISIBLE
                }else{
                    binding.jobsList.visibility = View.GONE
                    binding.textView.visibility = View.VISIBLE
                }
            }
        })
    }
}