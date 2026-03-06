package com.example.classschedule.analytical_method

import com.example.classschedule.data.Course

interface CourseParser {
    val schoolId: String
    val schoolName: String

    @Throws(Exception::class)
    fun parseHtml(rawHtml: String,onScheduleNotFound:()-> Unit): List<Course>
}

