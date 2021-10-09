package com.example.timetable

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.timetable.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {

    private val NUM_PAGES = 7
    private lateinit var binding: ActivityMainBinding
    private val dayMap = mapOf(
        0 to "Понедельник",
        1 to "Вторник",
        2 to "Среда",
        3 to "Четверг",
        4 to "Пятница",
        5 to "Суббота",
        6 to "Воскресенье"
    )

    private val time = Calendar.getInstance()
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.Default).launch {
            ApplicationSettings.setApplicationTheme(theme, this@MainActivity)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listsPager.apply {
            adapter = PagerFragmentAdapter(this@MainActivity)
        }
        binding.listsPager.setCurrentItem(getCurrentDayOfWeekIndex(), false)
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.listsPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.setText(dayMap.get(position))
                }
            })
        tabLayoutMediator.attach()

        binding.toolbar.title = resources.getText(R.string.app_action_default_title)
        setSupportActionBar(binding.toolbar)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_settings -> {
            CoroutineScope(Dispatchers.Default).launch {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
            }
            true
        }

        R.id.action_filter -> {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            true
        }
        else ->
            super.onOptionsItemSelected(item)
    }

    private fun getCurrentDayOfWeekIndex(): Int = when (time.get(Calendar.DAY_OF_WEEK)) {
        1 -> 6
        7 -> 5
        else -> time.get(Calendar.DAY_OF_WEEK) - 2
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    private inner class PagerFragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = NUM_PAGES

        override fun createFragment(position: Int): Fragment {

            return ListFragment(dayMap.get(position))
        }
    }

    override fun onRestart() {
        super.onRestart()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if(preferences.getBoolean("theme_changed", false)){
            preferences.edit().putBoolean("theme_changed", false).apply()
            ApplicationSettings.setApplicationTheme(theme, this)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}