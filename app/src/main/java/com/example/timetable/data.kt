package com.example.timetable

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "job_table")
data class Job(
    @PrimaryKey(autoGenerate = true)
    val uid: Long?,
    @ColumnInfo(name = "day_name") var dayName: String?,
    @ColumnInfo(name = "job_name") var jobName: String?,
    @ColumnInfo(name = "job_teacher") var jobTeacher: String?,
    @ColumnInfo(name = "startTime") var startTime: String?,
    @ColumnInfo(name = "endTime") var endTime: String?,
    @ColumnInfo(name = "classroom") var classroom: String?,
    @ColumnInfo(name = "even_week") var jobType: String?
    ):Serializable

val dayMap = mapOf(
    0 to "Понедельник",
    1 to "Вторник",
    2 to "Среда",
    3 to "Четверг",
    4 to "Пятница",
    5 to "Суббота",
    6 to "Воскресенье"
)

val NUM_PAGES = 7