@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH")

package com.example.classschedule.screen

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScheduleScreen(
    navigateUp: () -> Unit,
    universityUrl: String,
    viewModel: SpiderViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val statusText by viewModel.spiderStatus.collectAsState()

    var webViewInstance by remember { mutableStateOf<WebView?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = statusText, style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val jsCode = "(function() { return document.documentElement.outerHTML; })();"
                    webViewInstance?.evaluateJavascript(jsCode) { htmlStr ->
                        if (htmlStr != null && htmlStr != "null") {
                            viewModel.parseHtmlAndSave(htmlStr) {
                                navigateUp()
                            }
                        }
                    }
                },
                text = { Text("看到课表后，点我导入") },
                icon = { Icon(Icons.Default.Check, contentDescription = "导入") },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        webViewClient = WebViewClient()
                        webViewInstance = this
                        loadUrl(universityUrl)
                    }
                }
            )
        }
    }
}