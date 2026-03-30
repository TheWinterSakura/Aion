package com.example.classschedule.data.course

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val weekDay: String,
    val startWeekDate:Int,
    val endWeekDate: Int,
    var courseName: String,
    var courseTime: String,
    var courseCampus: String,
    var courseLocation: String,
    var courseTeacher: String,
    var courseTeachingClass: String,
    var courseTeachingClassComposition: String,
    var courseAssessmentMethods: String,
    var courseSelectionNotes: String,
    var courseHourComposition: String,
    var courseWeekStudyHours: String,
    var courseTotalStudyHours: String,
    var courseCredit: String,
)

data class CourseSimple(
    val courseName: String,
    val courseTime: String,
    val courseLocation: String,
    val weekDay: String,
    val id: Int,
)