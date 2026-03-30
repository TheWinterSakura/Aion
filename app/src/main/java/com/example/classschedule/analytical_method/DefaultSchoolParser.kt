package com.example.classschedule.analytical_method

import com.example.classschedule.data.course.Course
import com.example.classschedule.tools.getWeekDayString
import org.json.JSONTokener
import org.jsoup.Jsoup
import java.util.regex.Pattern

class DefaultSchoolParser : CourseParser {
    override val schoolId = "1"
    override val schoolName = "河北师范大学"

    override fun parseHtml(rawHtml: String, onScheduleNotFound: () -> Unit): List<Course> {

        val courseList = mutableListOf<Course>()

        val cleanHtml = JSONTokener(rawHtml).nextValue().toString()
        val document = Jsoup.parse(cleanHtml)

        val tdWraps = document.select("td.td_wrap")

        if (tdWraps.isEmpty()) {
            onScheduleNotFound()
        }

        for (td in tdWraps) {
            val tdId = td.attr("id")
            if (tdId.isEmpty() || !tdId.contains("-")) continue

            val dayOfWeekInt = tdId.split("-")[0].toIntOrNull() ?: continue
            val weekDayStr = getWeekDayString(dayOfWeekInt)

            val courseDivs = td.select("div.timetable_con")

            for (div in courseDivs) {
                val rawTitle = div.selectFirst("span.title")?.text() ?: div.selectFirst("u.title")?.text()?.trim()?:"未知课程"
                val proCourseName = regex.replace(rawTitle,"($1)")
                val courseName = proCourseName.replace("[★■☆]".toRegex(), "").trim()

                val timeInfo = div.selectFirst("p:has([title*=节/周])")?.text()?.trim() ?: ""
                val location = div.selectFirst("p:has([title*=上课地点])")?.text()?.trim() ?: ""
                val teacher = div.selectFirst("p:has([title*=教师])")?.text()?.trim() ?: ""
                val courseTeachingClass =
                    div.selectFirst("p:has([title*=教学班名称])")?.text()?.trim() ?: ""
                val courseTeachingClassComposition =
                    div.selectFirst("p:has([title*=教学班组成])")?.text()?.trim() ?: ""
                val courseAssessmentMethods =
                    div.selectFirst("p:has([title*=考核方式])")?.text()?.trim() ?: ""
                val courseSelectionNotes =
                    div.selectFirst("p:has([title*=选课备注])")?.text()?.trim() ?: ""
                val courseHourComposition =
                    div.selectFirst("p:has([title*=课程学时组成])")?.text()?.trim() ?: ""
                val courseWeekStudyHours =
                    div.selectFirst("p:has([title*=周学时])")?.text()?.trim() ?: ""
                val credit = div.selectFirst("p:has([title*=学分])")?.text()?.trim() ?: ""
                val totalHours = div.selectFirst("p:has([title*=总学时])")?.text()?.trim() ?: ""

                var startWeek = 0
                var endWeek = 0

                val weekMatcher = Pattern.compile("(\\d+)-?(\\d+)?周").matcher(timeInfo)
                if (weekMatcher.find()) {
                    startWeek = weekMatcher.group(1)?.toIntOrNull() ?: 0
                    endWeek = weekMatcher.group(2)?.toIntOrNull() ?: startWeek
                }

                val course = Course(
                    weekDay = weekDayStr,
                    startWeekDate = startWeek,
                    endWeekDate = endWeek,
                    courseName = courseName,
                    courseTime = timeInfo,
                    courseCampus = location.split(" ")[0],
                    courseLocation = location,
                    courseTeacher = teacher,
                    courseTeachingClass = courseTeachingClass,
                    courseTeachingClassComposition = courseTeachingClassComposition,
                    courseAssessmentMethods = courseAssessmentMethods,
                    courseSelectionNotes = courseSelectionNotes,
                    courseHourComposition = courseHourComposition,
                    courseWeekStudyHours = courseWeekStudyHours,
                    courseTotalStudyHours = totalHours,
                    courseCredit = credit
                )
                courseList.add(course)
            }
        }

        return courseList
    }

    val regex = Regex("【(.*?)】")

}