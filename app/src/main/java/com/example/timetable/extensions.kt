package com.example.timetable

import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

/*
*   basicly - just an extension function, which applying style to the current application theme,
*   depending on which style was chose in SettingsActivity.
*/

fun AppCompatActivity.setActivityTheme(){
    val pref = PreferenceManager.getDefaultSharedPreferences(this)
    when(pref.getString("theme_preference", "indigo_100")){
        "red_100"->theme.applyStyle(R.style.Theme_TimeTable_Red_100, true)
        "pink_100"->theme.applyStyle(R.style.Theme_TimeTable_Pink_100, true)
        "purple_100"->theme.applyStyle(R.style.Theme_TimeTable_Purple_100, true)
        "deep_purple_100"->theme.applyStyle(R.style.Theme_TimeTable_DeepPurple_100, true)
        "blue_100"->theme.applyStyle(R.style.Theme_TimeTable_Blue_100, true)
        "green_100"->theme.applyStyle(R.style.Theme_TimeTable_Green_100, true)
        "ember_100"->theme.applyStyle(R.style.Theme_TimeTable_Ember_100, true)
        "gray_100"->theme.applyStyle(R.style.Theme_TimeTable_Gray_100, true)
        "blue_gray_100"->theme.applyStyle(R.style.Theme_TimeTable_BlueGray_100, true)
        else->theme.applyStyle(R.style.Theme_TimeTable_Indigo_100, true)
    }
}