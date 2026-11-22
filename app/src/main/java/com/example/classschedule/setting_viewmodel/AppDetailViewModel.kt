package com.example.classschedule.setting_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classschedule.retrofit.ClassScheduleRemoteDataSource
import com.example.classschedule.retrofit.model.GitHubRelease
import com.example.classschedule.tools.showToast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppDetailViewModel @Inject constructor(
    private val remoteDataSource: ClassScheduleRemoteDataSource,
) : ViewModel() {

    val _Version = MutableStateFlow<GitHubRelease?>(null)
    val version = _Version.asStateFlow()

    fun checkVersion(){
        viewModelScope.launch {
            try {
                _Version.value = remoteDataSource.getAppVersion()
            }catch (e: Exception){
                e.printStackTrace()
                "获取失败，请稍后重试".showToast()
            }
        }
    }
}