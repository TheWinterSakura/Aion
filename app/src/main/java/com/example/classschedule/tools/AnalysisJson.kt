package com.example.classschedule.tools

import android.content.Context
import android.net.Uri
import android.widget.Toast
import kotlinx.serialization.json.Json
import java.io.BufferedReader
import java.io.InputStreamReader

object JsonTool {
    /**
     * 从指定的 Uri 读取 JSON 文件，并解析为指定的数据类
     * @param context 上下文
     * @param uri 用户选择的文件的 Uri
     * @return 解析成功返回数据对象 T，失败返回 null
     */
    inline fun <reified T> importJsonFromUri(context: Context, uri: Uri): T? {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val reader = BufferedReader(InputStreamReader(inputStream))
                val jsonString = reader.readText()

                val data = Json.decodeFromString<T>(jsonString)

                Toast.makeText(context, "解析成功", Toast.LENGTH_SHORT).show()
                data
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "解析失败: 格式错误或文件损坏", Toast.LENGTH_LONG).show()
            null
        }
    }
}