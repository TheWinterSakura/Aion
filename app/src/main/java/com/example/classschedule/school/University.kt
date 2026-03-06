package com.example.classschedule.school

import android.content.Context

data class University(
    val schoolName: String,
    val schoolUrl: String
)

fun loadUniversitiesFromAssets(context: Context, fileName: String): List<University> {
    val universityList = mutableListOf<University>()
    context.assets.open(fileName).bufferedReader().useLines { lines ->
        lines.drop(1)
            .filter { it.isNotBlank() }
            .forEach { line ->
                val parts = line.split(",")
                if (parts.size >= 2) {
                    val name = parts[0].trim()
                    val url = parts[1].trim().removeSurrounding("<", ">")
                    universityList.add(University(name, url))
                }
            }
    }
    return universityList
}