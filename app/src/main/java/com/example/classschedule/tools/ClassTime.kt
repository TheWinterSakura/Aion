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

//    val classStartTime: String = when (classStart) {
//        1 -> "08:00"
//        3 -> "09:45"
//        5 -> "11:20"
//        6 -> "14:00"
//        8 -> "15:35"
//        9 -> "16:35"
//        11 -> "19:00"
//        13 -> "20:35"
//        else -> {
//            "获取失败"
//        }
//    }
//    val classEndTime: String = when (classEnd) {
//        2 -> "09:30"
//        4 -> "11:15"
//        5 -> "12:05"
//        7 -> "15:30"
//        8 -> "16:20"
//        9 -> "17:15"
//        10 -> "18:05"
//        12 -> "20:30"
//        13 -> "21:20"
//        else -> {
//            "获取失败"
//        }
//    }