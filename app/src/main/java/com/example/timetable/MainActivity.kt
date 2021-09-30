package com.example.timetable

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timetable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val NUM_PAGES = 7
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.listsPager.apply {
            adapter = PagerFragmentAdapter(this@MainActivity)
        }

        binding.addButton.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }

    private inner class PagerFragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {
            when (position) {
                0 -> return ListFragment(position)
                1 -> return ListFragment(position)
                2 -> return ListFragment(position)
                3 -> return ListFragment(position)
                4 -> return ListFragment(position)
                5 -> return ListFragment(position)
                6 -> return ListFragment(position)
            }
            return ListFragment(position)
        }
    }
}