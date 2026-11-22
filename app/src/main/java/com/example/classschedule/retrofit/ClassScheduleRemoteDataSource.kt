package com.example.classschedule.retrofit

import com.example.classschedule.retrofit.model.GitHubRelease
import com.example.classschedule.retrofit.model.TextChatRequest
import com.example.classschedule.retrofit.model.VisionRequest
import com.example.classschedule.retrofit.model.VisionResponse
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 网络请求的统一入口。
 * ViewModel 只依赖这个类，而不是直接依赖 Retrofit 接口，便于替换和测试。
 */
@Singleton
class ClassScheduleRemoteDataSource @Inject constructor(
    private val dashScopeApi: DashScopeApi,
    private val gitHubApi: GitHubApi,
) {

    suspend fun analyzeImage(
        apiKey: String,
        request: VisionRequest
    ): VisionResponse = dashScopeApi.analyzeImage(apiKey = apiKey, request = request)

    suspend fun analyzeText(
        apiKey: String,
        request: TextChatRequest
    ): VisionResponse = dashScopeApi.analyzeText(apiKey = apiKey, request = request)

    suspend fun getAppVersion(): GitHubRelease = gitHubApi.getAppVersion()
}
