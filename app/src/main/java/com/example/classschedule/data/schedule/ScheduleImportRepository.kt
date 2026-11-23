package com.example.classschedule.data.schedule

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Base64
import com.example.classschedule.data.course.Course
import com.example.classschedule.retrofit.ClassScheduleRemoteDataSource
import com.example.classschedule.retrofit.model.ContentPart
import com.example.classschedule.retrofit.model.CourseWrapper
import com.example.classschedule.retrofit.model.ImageUrl
import com.example.classschedule.retrofit.model.TextChatRequest
import com.example.classschedule.retrofit.model.TextMessage
import com.example.classschedule.retrofit.model.VisionMessage
import com.example.classschedule.retrofit.model.VisionRequest
import com.google.gson.Gson
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import com.tom_roush.pdfbox.text.PDFTextStripper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 负责“智能导入课表”的数据处理：
 * - 图片 Uri → 压缩 → Base64
 * - PDF Uri → 文本
 * - 调用大模型并解析 JSON
 *
 * ViewModel 只拿 Uri 和结果，不接触 Bitmap / Context / 文件流。
 */
@Singleton
class ScheduleImportRepository @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val remoteDataSource: ClassScheduleRemoteDataSource,
    private val gson: Gson,
) {

    init {
        PDFBoxResourceLoader.init(context)
    }

    suspend fun importFromImages(
        uris: List<Uri>,
        apiKey: String,
    ): List<Course> = withContext(Dispatchers.IO) {
        require(uris.isNotEmpty()) { "请先选择课表图片" }

        val parts = buildList {
            add(ContentPart(type = "text", text = promptImage))
            uris.forEach { uri ->
                add(
                    ContentPart(
                        type = "image_url",
                        image_url = ImageUrl(uriToBase64(uri))
                    )
                )
            }
        }

        val messages = listOf(
            VisionMessage(
                "system",
                listOf(
                    ContentPart(
                        type = "text",
                        text = "你是一个精确的课表解析器，只输出JSON。"
                    )
                )
            ),
            VisionMessage("user", parts)
        )

        val response = remoteDataSource.analyzeImage(
            apiKey = apiKey,
            request = VisionRequest(messages = messages)
        )
        parseCourses(response.choices.firstOrNull()?.message?.content)
    }

    suspend fun importFromPdf(
        uri: Uri,
        apiKey: String,
    ): List<Course> = withContext(Dispatchers.IO) {
        val pdfText = context.contentResolver.openInputStream(uri)?.use { input ->
            PDDocument.load(input).use { document ->
                PDFTextStripper().getText(document)
            }
        } ?: throw IllegalStateException("无法打开 PDF 文件")

        if (pdfText.isBlank()) {
            throw IllegalStateException("未能从 PDF 中读取到文字内容")
        }

        val messages = listOf(
            TextMessage(
                role = "system",
                content = "你是一个精确的课表解析器，只输出JSON。"
            ),
            TextMessage(
                role = "user",
                content = "$promptText(这是内容：$pdfText)"
            )
        )

        val response = remoteDataSource.analyzeText(
            apiKey = apiKey,
            request = TextChatRequest(messages = messages)
        )
        parseCourses(response.choices.firstOrNull()?.message?.content)
    }

    private fun parseCourses(raw: String?): List<Course> {
        if (raw.isNullOrBlank()) {
            throw IllegalStateException("网络请求失败，未获取到内容")
        }

        val start = raw.indexOf('{')
        val end = raw.lastIndexOf('}')
        if (start == -1 || end == -1 || start > end) {
            throw IllegalStateException("大模型未返回有效的课表格式")
        }

        val wrapper = gson.fromJson(
            raw.substring(start, end + 1),
            CourseWrapper::class.java
        ) ?: throw IllegalStateException("JSON 解析失败")

        return wrapper.courses
    }

    private fun uriToBase64(uri: Uri): String {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            decoder.isMutableRequired = false

            // 限制最大边长，既能保证识别效果，也能避免大图占用过多内存
            val maxSize = 1600
            val width = info.size.width
            val height = info.size.height
            if (width > maxSize || height > maxSize) {
                val scale = maxSize.toFloat() / maxOf(width, height)
                decoder.setTargetSize(
                    (width * scale).toInt().coerceAtLeast(1),
                    (height * scale).toInt().coerceAtLeast(1)
                )
            }
        }

        return try {
            ByteArrayOutputStream().use { output ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, output)
                val base64 = Base64.encodeToString(output.toByteArray(), Base64.NO_WRAP)
                "data:image/jpeg;base64,$base64"
            }
        } finally {
            bitmap.recycle()
        }
    }

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
}
