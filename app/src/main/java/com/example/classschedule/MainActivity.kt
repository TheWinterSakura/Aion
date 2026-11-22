package com.example.classschedule

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.classschedule.navigation.MainNavScreenNav3
import com.example.classschedule.setting_viewmodel.ThemeColorViewModel
import com.example.classschedule.ui.theme.ClassScheduleTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val app = LocalContext.current.applicationContext as ClassScheduleApplication
            val isReady by app.isReady.collectAsStateWithLifecycle()
            val themeViewModel: ThemeColorViewModel = hiltViewModel()
            val themeColor by themeViewModel.themeColor.collectAsStateWithLifecycle()
            ClassScheduleTheme(themeColorName = themeColor) {
                Box(modifier = Modifier.fillMaxSize()) {

                    AnimatedVisibility(
                        visible = isReady,
                        enter = fadeIn(tween(300)),
                        exit = fadeOut()
                    ) {
                        MainNavScreenNav3(finishAffinity = { finishAffinity() })
                    }

                    AnimatedVisibility(
                        visible = !isReady,
                        enter = fadeIn(),
                        exit = fadeOut(tween(200))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
