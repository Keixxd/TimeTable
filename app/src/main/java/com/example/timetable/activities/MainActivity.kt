package com.example.timetable.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.core.view.get
import androidx.preference.PreferenceManager
import com.example.timetable.*
import com.example.timetable.adapters.PagerFragmentAdapter
import com.example.timetable.databinding.DrawerLayoutBinding
import com.example.timetable.model.TableParametersResultContract
import com.example.timetable.viewmodels.JobViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: DrawerLayoutBinding
    private val time = Calendar.getInstance()
    private lateinit var preferences: SharedPreferences
    private val viewModel by viewModels<JobViewModel>()
    private lateinit var startForResult: ActivityResultLauncher<String?>
    private var tablesList: List<String>? = null
    private lateinit var navView: MainActivityNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //retrieving databases list from app directory in coroutine

        CoroutineScope(Dispatchers.IO).launch{
            tablesList = getDatabasesNames()
        }

        /*
        *   because i didnt find another way (yet!) to change style of theme in runtime
        *   without re-creating an activity - this solution will be here
        *   also (idk bug or not) this works only in Dispatcher.Default context,
        *   my opinion to this - its somehow binded with function, because maybe
        *   context.theme.applyTheme is cannot work in Dispathcer.Main
        *   will check this later
        */

        registerForResultActivity()
        setActivityTheme()

        /*CoroutineScope(Dispatchers.Default).launch {
            setActivityTheme()
        }*/

        binding = DrawerLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //NavigationViewBehavior().onLoadItemsInNavigationView(tablesList)

        initViewModel()
        navView = MainActivityNavigationView(binding, this, viewModel)
        navView.onLoadItemsInNavigationView(tablesList)
        bindViews()
    }

    private fun initViewModel(){
        /*viewModel = ViewModelProvider(this, JobViewModelFactory(application))
            .get(JobViewModel::class.java)*/
        viewModel.getDatabaseNameObservable().value = tablesList!![0]
    }

    private fun bindViews() {

        // first: binding ViewPager and give it PagerAdapter

        binding.main.listsPager.apply {
            adapter = PagerFragmentAdapter(this@MainActivity, viewModel)
        }
        binding.main.listsPager.setCurrentItem(getCurrentDayOfWeekIndex(), false)

        //setting up tabLayout here

        val tabLayoutMediator = TabLayoutMediator(binding.main.tabLayout, binding.main.listsPager,
            object : TabLayoutMediator.TabConfigurationStrategy {
                override fun onConfigureTab(tab: TabLayout.Tab, position: Int) {
                    tab.setText(dayMap.get(position))
                }
            })
        tabLayoutMediator.attach()

        //setting up click listener to fab

        binding.main.addButton.setOnClickListener {
            startActivity(
                Intent(this, AddActivity::class.java).putExtra("databaseName",
                    viewModel.getDatabaseNameObservable().value))
        }

        //second: application bar

        binding.main.toolbar.title = resources.getText(R.string.app_action_default_title)
        setSupportActionBar(binding.main.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.main.toolbar,
            R.string.open,
            R.string.close
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //and then navigation view

        binding.navView.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.nav_add_table ->
                    navView.onBuildAddItemDialog()
                R.id.nav_settings -> {
                    val job = CoroutineScope(Dispatchers.Default).launch {
                        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                    }
                    if (job.isCompleted)
                        binding.drawerLayout.closeDrawers()
                }
            }
            true
        }

    }

    //logic for out application bar

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {

        R.id.action_filter -> {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            true
        }
        R.id.action_edit_table -> {
            startEditTableParamsActivity()
            true
        }
        else ->
            super.onOptionsItemSelected(item)
    }

    private fun registerForResultActivity(){
        startForResult = registerForActivityResult(TableParametersResultContract()){ result: String? ->
            when(result){
                activityResultsTypes.ACTIVITY_CANCELED.code -> {}
                activityResultsTypes.TABLE_DELETED.code -> {}
                activityResultsTypes.TABLE_CLEARED.code -> {binding.main.listsPager.adapter?.notifyDataSetChanged()}
                else -> {
                    updateNavViewMenu(result)
                    viewModel.getDatabaseNameObservable().value = result
                    binding.main.listsPager.adapter?.notifyDataSetChanged()
                }
            }
        }
    }

    private fun updateNavViewMenu(result: String?) {
        with(binding){
            navView.menu[0].subMenu.forEach {
                if(it.title.equals(viewModel.getDatabaseNameObservable().value))
                    it.title = result
            }
        }
    }

    private fun startEditTableParamsActivity() {
        startForResult.launch(viewModel.getDatabaseNameObservable().value)
    }

    /*
    *   Because Calendar's week starts with sunday, this function changes in the way,
    *   like they are starting with monday.
    */

    private fun getCurrentDayOfWeekIndex(): Int = when (time.get(Calendar.DAY_OF_WEEK)) {
        1 -> 6
        7 -> 5
        else -> time.get(Calendar.DAY_OF_WEEK) - 2
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    /*
    *   Here we just go to application data directory, retrieving files list,
    *   filter it and returning out databases names list.
    */
    private fun getDatabasesNames(): List<String> {
        val filesList = File(applicationContext.dataDir, "databases").listFiles()
        val newList = mutableListOf<String>()
        if (!filesList.isNullOrEmpty()) {
            for (file in filesList) {
                newList.add(file.name)
            }
            return newList.filter { !(it.contains("-shm") || it.contains("-wal")) }
        }else
            return mutableListOf("Расписание 1")
    }

    /*
    *   Every time we returning from settingsActivity, we check if our theme was changed.
    *   (i will find another way to check it, i dont like using shared preferences this way)
    */
    override fun onRestart() {
        super.onRestart()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
        if (preferences.getBoolean("theme_changed", false)) {
            preferences.edit().putBoolean("theme_changed", false).apply()
            setActivityTheme()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}