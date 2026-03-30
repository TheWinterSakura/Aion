package com.example.classschedule.data.schedule

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.classschedule.data.Course

@Database(entities = [Schedule::class], version = 4, exportSchema = false)
abstract class ScheduleDatabase : RoomDatabase() {
    abstract fun ScheduleDao(): ScheduleDao
    companion object {
        @Volatile
        private var Instance: ScheduleDatabase? = null

        fun getDatabase(context: Context): ScheduleDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScheduleDatabase::class.java, "schedule_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}