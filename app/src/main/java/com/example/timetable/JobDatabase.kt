package com.example.timetable

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Job::class], version = 1, exportSchema = false)
abstract class JobDatabase: RoomDatabase() {
    abstract fun jobDao(): JobDao

    companion object{
        @Volatile
        private var INSTANCE: JobDatabase? = null

        fun getDatabase(context: Context, dataBaseName: String): JobDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null)
                return tempInstance
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    JobDatabase::class.java,
                    dataBaseName
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}