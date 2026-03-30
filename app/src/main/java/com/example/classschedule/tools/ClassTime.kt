package com.example.classschedule.tools

import com.example.classschedule.data.schedule.Schedule
fun getClassTime(classTime: String, allCourseTime: List<Schedule>): String {
    val classStart: Int
    val classEnd: Int

    if (classTime.contains("-")) {
        classStart = classTime.substringBefore("-").substringAfter("(").toInt()
        classEnd = classTime.substringAfter("-").substringBefore("节").toInt()
    } else {
        classStart = classTime.substringBefore("节").substringAfter("(").toInt()
        classEnd = classTime.substringBefore("节").substringAfter("(").toInt()
    }

    val startTime = allCourseTime.find { it.courseNumber == classStart }?.startTime ?: "获取失败(请前往设置查看时间表是否正确)"
    val endTime = allCourseTime.find { it.courseNumber == classEnd }?.endTime ?: ""

    return "$startTime-$endTime"
}
