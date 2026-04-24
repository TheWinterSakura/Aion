package com.example.classschedule.tools

import android.content.Context
import android.net.Uri
import android.widget.Toast
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ExportClassSchedule {

    /**
     * 导出任意支持序列化的数据类到指定的 Uri 中
     * @param context 上下文，用于 ContentResolver 和 Toast
     * @param uri 用户选择的保存路径
     * @param data 要导出的泛型数据对象
     * @return Boolean 表示是否导出成功
     */
    inline fun <reified T> exportJsonToUri(
        context: Context,
        uri: Uri,
        data: T
    ): Boolean {
        return try {
            context.contentResolver.openOutputStream(uri)?.use { output ->
                val json = Json.encodeToString(data)
                output.write(json.toByteArray())
            }
            Toast.makeText(context, "导出成功", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "导出失败: ${e.message}", Toast.LENGTH_LONG).show()
            false
        }
    }
}