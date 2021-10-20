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

class ListFragment(val dayName: String?, val viewModel: JobViewModel): Fragment() {

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