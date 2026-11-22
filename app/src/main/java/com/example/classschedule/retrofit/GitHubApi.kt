package com.example.classschedule.retrofit

import com.example.classschedule.retrofit.model.GitHubRelease
import retrofit2.http.GET

/**
 * GitHub 接口。
 * baseUrl: https://api.github.com/
 */
interface GitHubApi {

    @GET("repos/TheWinterSakura/ClassSchedule/releases/latest")
    suspend fun getAppVersion(): GitHubRelease
}
