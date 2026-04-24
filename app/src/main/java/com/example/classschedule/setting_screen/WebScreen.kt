@file:Suppress("COMPOSE_APPLIER_CALL_MISMATCH", "DEPRECATION")

package com.example.classschedule.setting_screen

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
import com.example.classschedule.setting_viewmodel.SpiderViewModel

@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImportScheduleScreen(
    navigateUp: () -> Unit,
    universityUrl: String,
    viewModel: SpiderViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToHome:()-> Unit
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
                            viewModel.analytical(
                                schoolName = "河北师范大学",
                                rawHtml = htmlStr,
                                onParseSuccess = {
                                    navigateToHome()
                                }
                            )
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
                        settings.allowFileAccess = false
                        settings.allowContentAccess = false
                        settings.allowFileAccessFromFileURLs = false
                        settings.allowUniversalAccessFromFileURLs = false
                        settings.mixedContentMode = android.webkit.WebSettings.MIXED_CONTENT_NEVER_ALLOW

                        isHorizontalScrollBarEnabled = true
                        isVerticalScrollBarEnabled = true
                        settings.useWideViewPort = true
                        settings.loadWithOverviewMode = false
                        settings.setSupportZoom(true)
                        settings.builtInZoomControls = true
                        settings.displayZoomControls = false

                        webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: android.webkit.WebResourceRequest?
                            ): Boolean {
                                val url = request?.url?.toString() ?: return true
                                return if (url.startsWith("https://") || url.startsWith("http://")) {
                                    false
                                } else {
                                    true
                                }
                            }
                        }
                        
                        webViewInstance = this
                        if (universityUrl.startsWith("http://") || universityUrl.startsWith("https://")) {
                            loadUrl(universityUrl)
                        }
                    }
                }
            )
        }
    }
}