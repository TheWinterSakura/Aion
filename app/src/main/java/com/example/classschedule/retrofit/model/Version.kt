package com.example.classschedule.retrofit.model

import com.google.gson.annotations.SerializedName

data class GitHubRelease(
    @SerializedName("tag_name")
    val tagName: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("body")
    val body: String,

    @SerializedName("published_at")
    val publishedAt: String,

    @SerializedName("html_url")
    val htmlUrl: String,

    @SerializedName("assets")
    val assets: List<ReleaseAsset>
)

data class ReleaseAsset(
    @SerializedName("name")
    val name: String,

    @SerializedName("size")
    val size: Long,

    @SerializedName("browser_download_url")
    val browserDownloadUrl: String,

    @SerializedName("content_type")
    val contentType: String
)
