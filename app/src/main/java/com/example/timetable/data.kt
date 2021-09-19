package com.example.timetable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "job_table")
data class Job(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,
    @ColumnInfo(name = "day_name") var dayName: String?,
    @ColumnInfo(name = "job_name") var jobName: String?,
    @ColumnInfo(name = "job_teacher") var jobTeacher: String?,
    @ColumnInfo(name = "time") var time: String?,
    @ColumnInfo(name = "classroom") var classroom: String?,
    @ColumnInfo(name = "even_week") var evenWeek: Boolean
    )