package com.example.classschedule.tools

import android.content.Context
import android.widget.Toast
import com.example.classschedule.ClassScheduleApplication

fun String.showToast(
    context: Context = ClassScheduleApplication.context,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, this, length).show()
}
