package com.example.classschedule.di

import com.example.classschedule.retrofit.DashScopeApi
import com.example.classschedule.retrofit.GitHubApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val DASH_SCOPE_BASE_URL = "https://dashscope.aliyuncs.com/"
    private const val GITHUB_BASE_URL = "https://api.github.com/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    fun provideDashScopeApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): DashScopeApi = createRetrofit(
        baseUrl = DASH_SCOPE_BASE_URL,
        okHttpClient = okHttpClient,
        gson = gson,
    ).create(DashScopeApi::class.java)

    @Provides
    @Singleton
    fun provideGitHubApi(
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): GitHubApi = createRetrofit(
        baseUrl = GITHUB_BASE_URL,
        okHttpClient = okHttpClient,
        gson = gson,
    ).create(GitHubApi::class.java)

    private fun createRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
}
