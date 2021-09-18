package com.example.timetable

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.timetable.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

/*    private val sunday = 1000
    private val monday = 1001
    private val tuesday = 1002
    private val wednesday = 1003
    private val thursday = 1004
    private val friday = 1005
    private val saturday = 1006*/

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = PagerAdapter(this)

        binding.listsPager.apply {
            adapter = pagerAdapter
        }
    }
}