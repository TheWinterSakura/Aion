package com.example.classschedule.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class HomeViewModel: ViewModel(){
    val week = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    fun simpleQueryAllCourse(
        weekData: String,
        weekDay: String
    ){
        viewModelScope.launch {

        }
    }
}