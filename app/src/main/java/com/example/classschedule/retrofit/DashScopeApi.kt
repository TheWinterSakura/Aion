package com.example.classschedule.retrofit

import com.example.classschedule.retrofit.model.TextChatRequest
import com.example.classschedule.retrofit.model.VisionRequest
import com.example.classschedule.retrofit.model.VisionResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * 阿里云 DashScope（通义千问）接口。
 * baseUrl: https://dashscope.aliyuncs.com/
 */
interface DashScopeApi {

    @Headers("Content-Type: application/json")
    @POST("compatible-mode/v1/chat/completions")
    suspend fun analyzeImage(
        @Header("Authorization") apiKey: String,
        @Body request: VisionRequest
    ): VisionResponse

    @Headers("Content-Type: application/json")
    @POST("compatible-mode/v1/chat/completions")
    suspend fun analyzeText(
        @Header("Authorization") apiKey: String,
        @Body request: TextChatRequest
    ): VisionResponse
}
