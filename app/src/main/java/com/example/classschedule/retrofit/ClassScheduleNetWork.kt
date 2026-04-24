package com.example.classschedule.retrofit

import com.example.classschedule.retrofit.model.GitHubRelease
import com.example.classschedule.retrofit.model.TextChatRequest
import com.example.classschedule.retrofit.model.VisionRequest
import com.example.classschedule.retrofit.model.VisionResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface ClassScheduleApi {

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

    @GET("repos/TheWinterSakura/ClassSchedule/releases/latest")
    suspend fun getAppVersion(): GitHubRelease

}

object ClassScheduleNetWork {
    private const val BASE_URL = "https://dashscope.aliyuncs.com/"
    private const val GIT_BASE_URL = "https://api.github.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    val retrofitService: ClassScheduleApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClassScheduleApi::class.java)
    }

    val retrofitServiceGit: ClassScheduleApi by lazy {
        Retrofit.Builder()
            .baseUrl(GIT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ClassScheduleApi::class.java)
    }


    suspend fun analyzeImage(
        apiKey: String,
        request: VisionRequest
    ): VisionResponse {
        return retrofitService.analyzeImage(apiKey = apiKey, request = request)
    }

    suspend fun analyzeText(
        apiKey: String,
        request: TextChatRequest
    ): VisionResponse {
        return retrofitService.analyzeText(apiKey = apiKey, request = request)
    }

    suspend fun getAppVersion(): GitHubRelease {
        return retrofitServiceGit.getAppVersion()
    }

}

