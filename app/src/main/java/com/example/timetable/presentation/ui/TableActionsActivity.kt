package com.example.timetable.presentation.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.timetable.R
import com.example.timetable.data.repository.FileManager
import com.example.timetable.databinding.TableParamsActivityBinding
import com.example.timetable.utils.setActivityTheme
import com.example.timetable.presentation.viewmodels.JobViewModel
import java.io.IOException

class TableActionsActivity: AppCompatActivity() {

    private lateinit var binding: TableParamsActivityBinding
    private var receivedTableName: String? = null
    private val viewModel by viewModels<JobViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityTheme()
        binding = TableParamsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receivedTableName = getDataFromIntent()
        viewModel.getDatabaseNameObservable().value = receivedTableName
        setContainersData()
        bindButtons()
    }

    private fun bindButtons(){
        with(binding){
            clearTimetableCard.setOnClickListener{
                viewModel.clearTable()
                setResult(Activity.RESULT_OK, onPackIntent("TABLE_CLEARED"))
                finish()
            }
            deleteTableCard.setOnClickListener{
                try {
                    FileManager(this@TableActionsActivity)
                        .onDeleteFile(receivedTableName)
                    setResult(Activity.RESULT_OK, onPackIntent("TABLE_DELETED"))
                    finish()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getDataFromIntent(): String? = intent.getStringExtra("databaseName")

    private fun setContainersData() {
        with(binding) {
            timetableEtName.setText(receivedTableName)
            setSupportActionBar(tableParamsToolBar)
            supportActionBar?.setTitle("Параметры")
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_params -> {
            if(binding.timetableEtName.text.isNotBlank()) {
                val resultTableName = onSaveParams()
                setResult(Activity.RESULT_OK, onPackIntent(resultTableName))
                finish()
            }else{
                showErrorDrawables()
                Toast.makeText(this, "Заполните выделенные поля", Toast.LENGTH_LONG).show()
            }
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onSaveParams():String {
        val resultTableName = binding.timetableEtName.text.toString()
        val fileManager = FileManager(this)
        if(resultTableName != receivedTableName) {
            try {
                fileManager.onRewriteFileName(receivedTableName, resultTableName)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            onSupportNavigateUp()
        }
        return resultTableName
    }

    private fun onPackIntent(result: String): Intent {
        val resultIntent = Intent()
        resultIntent.putExtra("table_params_activity_result", result)
        return resultIntent
    }

    private fun showErrorDrawables(){
        with(binding) {

            val checkState: (EditText) -> Unit = { et ->
                if(et.text.isBlank())
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,R.drawable.ic_baseline_error_24, 0)
                else
                    et.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0,0, 0)
            }

            checkState(timetableEtName)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_table_params_activity_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        setResult(Activity.RESULT_CANCELED)
        finish()
        return true
    }
}