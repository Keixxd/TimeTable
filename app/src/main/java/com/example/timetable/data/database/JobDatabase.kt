package com.example.timetable.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.timetable.Job


@Database(entities = [Job::class], version = 1, exportSchema = false)
abstract class JobDatabase: RoomDatabase() {
    abstract fun jobDao(): JobDao

    companion object{
        @Volatile
        private var INSTANCE: JobDatabase? = null

        fun getDatabase(context: Context, dataBaseName: String): JobDatabase {
            val tempInstance = INSTANCE
            if(tempInstance != null && tempInstance.openHelper.databaseName.equals(dataBaseName))
                return tempInstance
            synchronized(this){
                tempInstance?.close()
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