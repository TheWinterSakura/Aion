package com.example.classschedule.tools

import com.example.classschedule.data.schedule.Schedule

fun getClassTime(classTime: String, allCourseTime: List<Schedule>): String {
    val classStart: Int
    val classEnd: Int

    try {
        val content = classTime.substringAfter("(").substringBefore(")")
        if (content.contains("-")) {
            val parts = content.split("-")
            classStart = parts[0].trim().toInt()
            classEnd = parts[1].replace("节", "").trim().toInt()
        } else {
            val number = content.replace("节", "").trim().toInt()
            classStart = number
            classEnd = number
        }
    } catch (e: Exception) {
        return classTime
    }

    val startTime = allCourseTime.find { it.courseNumber == classStart }?.startTime ?: "请前往设置完成时间表"
    val endTime = allCourseTime.find { it.courseNumber == classEnd }?.endTime ?: ""

    return if (endTime.isNotEmpty()) "$startTime-$endTime" else startTime
}
