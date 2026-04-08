package com.example.classschedule.data.course

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Course::class, CourseTable::class], version = 6, exportSchema = false)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun courseTableDao(): CourseTableDao
    companion object {
        @Volatile
        private var Instance: CourseDatabase? = null

        fun getDatabase(context: Context): CourseDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, CourseDatabase::class.java, "course_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}