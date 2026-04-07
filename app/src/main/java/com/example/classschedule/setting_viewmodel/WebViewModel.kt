package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.analytical_method.ParserFactory
import com.example.classschedule.data.course.CourseRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SpiderViewModel(
    private val repository: CourseRepository,
) : ViewModel() {

    private val _spiderStatus = MutableStateFlow("请登录，看到课表后点击右下角导入")
    val spiderStatus: StateFlow<String> = _spiderStatus

    fun analytical(
        schoolName: String,
        rawHtml: String,
        onParseSuccess: () -> Unit
    ) {
        _spiderStatus.value = "✅ 获取成功，正在解析中..."
        viewModelScope.launch {
            try {
                val parser = ParserFactory.getParser(schoolName = schoolName)
                val courseList = parser.parseHtml(
                    rawHtml = rawHtml,
                    onScheduleNotFound = {
                        _spiderStatus.value = "❌ 解析失败：没找到课表！请确保课表已完全显示出来！"
                    })
                courseList.forEach { course ->
                    repository.insertCourse(course)
                }
                _spiderStatus.value = "🎉 导入成功！共获取 ${courseList.size} 节课程！"

                delay(1000)
                onParseSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
                _spiderStatus.value = "❌ 解析出错: ${e.message}"
            }
        }
    }
}