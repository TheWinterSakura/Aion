package com.example.classschedule.setting_screen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.data.Course
import com.example.classschedule.data.CourseRepository
import com.example.classschedule.data.UserPreferencesRepository
import com.example.classschedule.retrofit.ClassScheduleNetWork
import com.example.classschedule.retrofit.model.ContentPart
import com.example.classschedule.retrofit.model.CourseWrapper
import com.example.classschedule.retrofit.model.ImageUrl
import com.example.classschedule.retrofit.model.TextChatRequest
import com.example.classschedule.retrofit.model.TextMessage
import com.example.classschedule.retrofit.model.VisionMessage
import com.example.classschedule.retrofit.model.VisionRequest
import com.example.classschedule.tools.showToast
import com.google.gson.Gson
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

data class UiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
)

class IdentifyViewModel(
    private val repository: CourseRepository,
    private val userRepository: UserPreferencesRepository
) : ViewModel() {

    private val gson = Gson()
    val apiKey = userRepository.apiKey.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Companion.WhileSubscribed(5_000),
        initialValue = ""
    )

    var courseList = mutableStateListOf<Course>()

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val promptImage = """
        你是一个智能课表提取助手。请分析这张课表图片，提取所有课程信息。
        要求：
        1. 严格返回一个包含 `courses` 数组的纯 JSON 对象。
        2. 缺失的信息（如考核方式、选课备注等）请填入空字符串 ""。
        3. 周数请提取为整数（如 startWeekDate: 1, endWeekDate: 16）。
        4. 只提取明确是“课程”的信息！遇到不是课程的内容（如页眉、页脚、空白）请直接跳过！
        5. 如果没有识别到任何课程，请返回 {"courses": []}。
        6. 星期几使用英文，并且首字母大写
        7. courseTime参数返回时,需要使用英文括号将第几节到第几节括起来，后面是课程从几周到几周。
        8. 必须严格遵守以下 JSON 结构，不要输出任何多余的解释文字：
        {
          "courses": [
            {
              "weekDay": "Monday", "startWeekDate": 1, "endWeekDate": 16,
              "courseName": "高等数学", "courseTime": "(1-4节)1-5周", "courseCampus": "裕华校区",
              "courseLocation": "裕华校区 理科群1号楼A505", "courseTeacher": "张三", "courseTeachingClass": "持久化框架开发-0002",
              "courseTeachingClassComposition": "23软件工程5班;23软件工程6班;23软件工程7班;23软件工程8班", "courseAssessmentMethods": "未安排",
              "courseSelectionNotes": "", "courseHourComposition": "理论32",
              "courseWeekStudyHours": "2", "courseTotalStudyHours": "32", "courseCredit": "2.0"
            }
          ]
        }
    """.trimIndent()

    private val promptText = """
    【任务目标】
    你是一个无感情的JSON生成机器。请从以下杂乱的课表文本中，精准提取所有课程信息，并严格按照规定结构输出纯JSON字符串。

    【极其严格的格式要求】
    1. 必须且只能输出一个包含 `courses` 数组的纯JSON对象！
    2. 绝对禁止输出任何前言、后语、提示性文字！
    3. 绝对禁止使用Markdown格式包裹数据（绝对不要出现 ```json 和 ``` 标记）！输出必须直接以 { 开头，以 } 结尾。
    4. 如果没有识别到任何有效课程，或者全是页眉页脚，必须输出默认结果：{"courses": []}

    【核心去重规则（非常重要）】
    如果两节课的 [课程名称、星期几、节次、周数] 完全一样，或者包含了完全重叠的信息，请【务必合并为一条记录】，绝对不要在 JSON 中输出重复的课程实体！
    例如周一出现的和周二出现的同名课，但是他们不在同一天不能合并为一节课，而是两节不同的课

    【字段提取规则】
    - `courseTime`：格式必须严格遵守 "(第a-b节)c-d周" 的结构，注意英文括号。例如："(1-4节)1-5周"。
    - `weekDay`：必须是标准的英文（如 "Monday", "Tuesday"）。
    - 周数：`startWeekDate` 和 `endWeekDate` 必须是纯数字（Int类型，如 1 和 16）。
    - 缺失的字段：未提及的信息（如选课备注、考核方式等）一律填入空字符串 ""，不能省略该字段，也不能输出 null。
    - 过滤杂讯：遇到非课程内容（页码、姓名、空白）直接跳过。

    【强制遵守的JSON结构模板】
    {
      "courses": [
        {
          "weekDay": "Monday",
          "startWeekDate": 1,
          "endWeekDate": 5,
          "courseName": "高等数学",
          "courseTime": "(1-4节)1-5周",
          "courseCampus": "裕华校区",
          "courseLocation": "理科群1号楼A505",
          "courseTeacher": "张三",
          "courseTeachingClass": "持久化框架开发-0002",
          "courseTeachingClassComposition": "23软件工程5班",
          "courseAssessmentMethods": "未安排",
          "courseSelectionNotes": "",
          "courseHourComposition": "理论32",
          "courseWeekStudyHours": "2",
          "courseTotalStudyHours": "32",
          "courseCredit": "2.0"
        }
      ]
    }
""".trimIndent()

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
        val base64Str = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
        return "data:image/jpeg;base64,$base64Str"
    }

    fun parseScheduleImage(bitmap: Bitmap , apiKey: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, success = false)
            try {
                val base64Image = bitmapToBase64(bitmap)
                val messages = listOf(
                    VisionMessage("system", listOf(ContentPart(type = "text", text = "你是一个精确的课表解析器，只输出JSON。"))),
                    VisionMessage("user", listOf(
                        ContentPart(type = "image_url", image_url = ImageUrl(base64Image)),
                        ContentPart(type = "text", text = promptImage)
                    ))
                )

                val response = ClassScheduleNetWork.analyzeImage(apiKey, VisionRequest(messages = messages))
                val jsonResult = response.choices.firstOrNull()?.message?.content

                handleJsonResult(jsonResult)

            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "解析失败: ${e.message}")
            }
        }
    }

    fun parseScheduleFromPdf(context: Context, uri: Uri , apiKey: String) {
        PDFBoxResourceLoader.init(context)

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null, success = false)

            try {
                val pdfText = context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    PDDocument.load(inputStream).use { document ->
                        val stripper = PDFTextStripper()
                        stripper.getText(document)
                    }
                }

                if (!pdfText.isNullOrBlank()) {
                    processTextAnalysis(pdfText , apiKey = apiKey )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "未能从 PDF 中读取到文字内容")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "PDF读取失败: ${e.message}")
            }
        }
    }

    private suspend fun processTextAnalysis(classText: String , apiKey: String) {
        try {
            val messages = listOf(
                TextMessage(
                    role = "system",
                    content = "你是一个精确的课表解析器，只输出JSON。"
                ),
                TextMessage(
                    role = "user",
                    content = "$promptText(这是内容：$classText)"
                )
            )

            val response = ClassScheduleNetWork.analyzeText(apiKey, TextChatRequest(messages = messages))
            val jsonResult = response.choices.firstOrNull()?.message?.content

            handleJsonResult(jsonResult)

        } catch (e: Exception) {
            e.printStackTrace()
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "解析失败: ${e.message}")
        }
    }

    private suspend fun handleJsonResult(jsonResult: String?) {
        if (jsonResult != null) {
            Log.d("Qwen_JSON", "大模型原始返回: $jsonResult")
            val startIndex = jsonResult.indexOf("{")
            val endIndex = jsonResult.lastIndexOf("}")

            if (startIndex != -1 && endIndex != -1 && startIndex <= endIndex) {
                val pureJson = jsonResult.substring(startIndex, endIndex + 1)
                try {
                    val parsedData = gson.fromJson(pureJson, CourseWrapper::class.java)
                    if (parsedData?.courses != null) {
                        withContext(Dispatchers.Main) {
                            courseList.clear()
                            courseList.addAll(parsedData.courses)
                        }
                        _uiState.value = _uiState.value.copy(isLoading = false, success = true)
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "未识别到课程信息")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "JSON解析失败: 格式异常")
                }
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "大模型未返回有效的课表格式")
            }
        } else {
            _uiState.value = _uiState.value.copy(isLoading = false, errorMessage = "网络请求失败，未获取到内容")
        }
    }

    fun addCourse() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                courseList.forEach { course ->
                    repository.insertCourse(course)
                }
                withContext(Dispatchers.Main) {
                    "添加成功".showToast()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    "添加失败".showToast()
                }
            }
        }
    }

    fun changeApiKey(
        apiKey: String
    ){
        viewModelScope.launch {
            userRepository.saveApiKey(
                apiKey = apiKey
            )
        }
    }
}