package com.example.classschedule.data.schedule

import android.net.Uri
import com.example.classschedule.data.course.Course


interface ScheduleImporter {
    suspend fun importFromImages(uris: List<Uri>, apiKey: String): List<Course>
    suspend fun importFromPdf(uri: Uri, apiKey: String): List<Course>
}
