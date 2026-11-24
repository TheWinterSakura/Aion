package com.example.classschedule.tools

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters


fun getDayAfterWeeks(startDateStr: String, weeksPassed: Long, dayOfWeek: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val startDate = LocalDate.parse(startDateStr, formatter)
    val day = when (dayOfWeek) {
        "Monday" -> DayOfWeek.MONDAY
        "Tuesday" -> DayOfWeek.TUESDAY
        "Wednesday" -> DayOfWeek.WEDNESDAY
        "Thursday" -> DayOfWeek.THURSDAY
        "Friday" -> DayOfWeek.FRIDAY
        "Saturday" -> DayOfWeek.SATURDAY
        "Sunday" -> DayOfWeek.SUNDAY
        else -> return ""
    }

    val target = startDate.plusWeeks(weeksPassed).with(TemporalAdjusters.previousOrSame(day))

    return target.format(formatter)
}