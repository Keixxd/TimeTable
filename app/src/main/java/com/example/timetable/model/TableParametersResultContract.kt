package com.example.timetable.model

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.example.timetable.activities.TableParametersActivity

class TableParametersResultContract: ActivityResultContract<String, String?>() {

    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, TableParametersActivity::class.java)
            .putExtra("databaseName", input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? = when {

        resultCode != Activity.RESULT_OK -> null

        else -> intent?.getStringExtra("table_params_activity_result")
    }
}