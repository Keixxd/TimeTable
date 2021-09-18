package com.example.timetable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Job(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "job_name") val jobName: String?,
    @ColumnInfo(name = "time") val time: String?,
    @ColumnInfo(name = "classroom") val classroom: String?
    )