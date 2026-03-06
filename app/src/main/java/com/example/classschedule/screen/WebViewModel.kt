package com.example.classschedule.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.Course
import com.example.classschedule.data.CourseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONTokener
import org.jsoup.Jsoup
import java.util.regex.Pattern

class SpiderViewModel(
    private val repository: CourseRepository,
) : ViewModel() {

    private val _spiderStatus = MutableStateFlow("请登录，看到课表后点击右下角导入")
    val spiderStatus: StateFlow<String> = _spiderStatus

    fun parseHtmlAndSave(rawHtml: String, onParseSuccess: () -> Unit) {
        _spiderStatus.value = "✅ 获取成功，正在解析中..."

        viewModelScope.launch {
            try {
                val cleanHtml = JSONTokener(rawHtml).nextValue().toString()

                val document = Jsoup.parse(cleanHtml)
                val courseList = mutableListOf<Course>()

                val tdWraps = document.select("td.td_wrap")

                if (tdWraps.isEmpty()) {
                    _spiderStatus.value = "❌ 解析失败：没找到课表！请确保课表已完全显示出来！"
                    return@launch
                }

                for (td in tdWraps) {
                    val tdId = td.attr("id")
                    if (tdId.isEmpty() || !tdId.contains("-")) continue

                    val dayOfWeekInt = tdId.split("-")[0].toIntOrNull() ?: continue
                    val weekDayStr = getWeekDayString(dayOfWeekInt)

                    val courseDivs = td.select("div.timetable_con")

                    for (div in courseDivs) {
                        val rawTitle = div.selectFirst("span.title")?.text() ?: "未知课程"
                        val courseName = rawTitle.replace("[★■☆]".toRegex(), "").trim()

                        val timeInfo = div.selectFirst("p:has([title*=节/周])")?.text()?.trim() ?: ""
                        val location = div.selectFirst("p:has([title*=上课地点])")?.text()?.trim() ?: ""
                        val teacher = div.selectFirst("p:has([title*=教师])")?.text()?.trim() ?: ""
                        val courseTeachingClass = div.selectFirst("p:has([title*=教学班名称])")?.text()?.trim() ?: ""
                        val courseTeachingClassComposition = div.selectFirst("p:has([title*=教学班组成])")?.text()?.trim() ?: ""
                        val courseAssessmentMethods = div.selectFirst("p:has([title*=考核方式])")?.text()?.trim() ?: ""
                        val courseSelectionNotes = div.selectFirst("p:has([title*=选课备注])")?.text()?.trim() ?: ""
                        val courseHourComposition = div.selectFirst("p:has([title*=课程学时组成])")?.text()?.trim() ?: ""
                        val courseWeekStudyHours = div.selectFirst("p:has([title*=周学时])")?.text()?.trim() ?: ""
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

                courseList.forEach { course ->
                    repository.insertCourse(course)
                }

                _spiderStatus.value = "🎉 导入成功！共获取 ${courseList.size} 节课程！"

                kotlinx.coroutines.delay(1000)
                onParseSuccess()

            } catch (e: Exception) {
                e.printStackTrace()
                _spiderStatus.value = "❌ 解析出错: ${e.message}"
            }
        }
    }

    private fun getWeekDayString(dayInt: Int): String {
        return when (dayInt) {
            1 -> "Monday"
            2 -> "Tuesday"
            3 -> "Wednesday"
            4 -> "Thursday"
            5 -> "Friday"
            6 -> "Saturday"
            7 -> "Sunday"
            else -> "Unknown"
        }
    }
}