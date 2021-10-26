package com.example.timetable.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.timetable.R
import com.example.timetable.databinding.TableParamsActivityBinding
import com.example.timetable.setActivityTheme
import java.io.File

class TableParametersActivity: AppCompatActivity() {

    private lateinit var binding: TableParamsActivityBinding
    private var receivedTableName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setActivityTheme()
        binding = TableParamsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        receivedTableName = getDataFromIntent()
        setContainersData()
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
            val resultTableName = onSaveParams()
            val resultIntent = Intent()
            resultIntent.putExtra("table_params_activity_result", resultTableName)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onSaveParams():String {
        val resultTableName = binding.timetableEtName.text
        if(!resultTableName.equals(receivedTableName)) {
            try {
            File(dataDir, "databases/${resultTableName}").createNewFile()
            val file = findFile()
                val newFile = File(dataDir, "databases/${resultTableName}")
                file?.copyTo(newFile, true)
                file?.delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }else{
            onSupportNavigateUp()
        }
        return resultTableName.toString()
    }

    private fun findFile(): File? = File(dataDir, "databases")
        .listFiles()
        ?.filter { it.name.equals(receivedTableName) }
        ?.get(0)

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