package com.example.classschedule.retrofit.model

import com.example.classschedule.data.Course


data class CourseWrapper(val courses: List<Course>)

data class VisionRequest(
    val model: String = "qwen3.5-plus",
    val messages: List<VisionMessage>,
    val response_format: Map<String, String> = mapOf("type" to "json_object")
)

data class VisionMessage(val role: String, val content: List<ContentPart>)
data class ContentPart(val type: String, val text: String? = null, val image_url: ImageUrl? = null)
data class ImageUrl(val url: String)


data class VisionResponse(val choices: List<Choice>)
data class Choice(val message: ResponseMessage)
data class ResponseMessage(val role: String, val content: String)

data class TextMessage(
    val role: String,
    val content: String
)

data class TextChatRequest(
    val model: String = "MiniMax-M2.5",
    val messages: List<TextMessage>,
    val temperature: Double = 0.01,
)