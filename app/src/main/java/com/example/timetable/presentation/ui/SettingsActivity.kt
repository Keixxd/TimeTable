package com.example.timetable.presentation.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.timetable.R
import com.example.timetable.databinding.SettingsActivityBinding
import com.example.timetable.utils.setActivityTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: SettingsActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.Default) {
            setActivityTheme()
        }

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.settingsToolBar)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setTitle(getString(R.string.settings_title))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val themePreference: ListPreference? = preferenceManager.findPreference("theme_preference")

            themePreference?.setOnPreferenceChangeListener(object: Preference.OnPreferenceChangeListener{
                override fun onPreferenceChange(preference: Preference?, newValue: Any?): Boolean {
                    val pref = PreferenceManager.getDefaultSharedPreferences(activity?.baseContext)
                    val editor = pref.edit()

                    editor.putBoolean("theme_changed", true)
                    editor.putString("theme_preference", newValue.toString())
                    editor.apply()

                    val intent = Intent(activity?.baseContext, SettingsActivity::class.java)
                    startActivity(intent)
                    activity?.overridePendingTransition(0,0)
                    activity?.finish()
                    return false
                }
            })
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}