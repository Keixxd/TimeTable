package com.example.timetable

import androidx.room.Query

interface JobDao {
    @Query("SELECT * FROM job WHERE uid IN (:jobIds)")
    fun loadById(jobIds: IntArray): List<Job>
}