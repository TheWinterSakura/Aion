package com.example.classschedule.setting_screen

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.SystemUpdateAlt
import androidx.compose.material.icons.outlined.Cloud
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.retrofit.model.GitHubRelease
import com.example.classschedule.setting_viewmodel.AppDetailViewModel
import com.example.classschedule.tools.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetail(
    navigateUp: () -> Unit,
    viewModel: AppDetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val versionName = remember { getAppVersionName(context) }

    val version by viewModel.version.collectAsState()

    var isCheckingVersion by remember { mutableStateOf(false) }
    var showVersionDialog by remember { mutableStateOf(false) }


    LaunchedEffect(version, isCheckingVersion) {
        if (isCheckingVersion && version != null) {
            val remoteVersion = version!!.tagName.removePrefix("v")

            if (remoteVersion != versionName) {
                showVersionDialog = true
            } else {
                "当前已是最新版本".showToast()
            }
            isCheckingVersion = false
        }
    }

    if (showVersionDialog && version != null) {
        VersionDialog(
            onDismiss = { showVersionDialog = false },
            version = version!!,
            context = context,
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("关于") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LetterCircle(
                    letter = "C",
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ClassSchedule",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "一款基于 Room 数据库实现的轻量级课程表软件",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }

            Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                )
            ) {
                Column {
                    ListItem(
                        headlineContent = { Text("GitHub 仓库") },
                        supportingContent = { Text("查看源代码及提交 Issue") },
                        leadingContent = { Icon(Icons.Outlined.Code, contentDescription = null) },
                        modifier = Modifier.clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/TheWinterSakura/ClassSchedule"))
                            context.startActivity(intent)
                        }
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        thickness = 0.5.dp,
                        color = MaterialTheme.colorScheme.outlineVariant
                    )

                    ListItem(
                        headlineContent = { Text("当前版本") },
                        supportingContent = { Text("v$versionName") },
                        leadingContent = { Icon(Icons.Outlined.Info, contentDescription = null) }
                    )

                    ListItem(
                        headlineContent = { Text("检查更新") },
                        supportingContent = {
                            if (isCheckingVersion) Text("正在检查中...") else null
                        },
                        leadingContent = { Icon(Icons.Outlined.Cloud, contentDescription = null) },
                        modifier = Modifier.clickable(
                            enabled = !isCheckingVersion
                        ) {
                            isCheckingVersion = true
                            viewModel.checkVersion()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "© 2023 TheWinterSakura",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun LetterCircle(
    letter: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun VersionDialog(
    onDismiss: () -> Unit,
    version: GitHubRelease,
    context: Context
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("发现新版本: ${version.tagName}") },
        text = {
            Column {
                Text(
                    text = "是否前往下载最新版本？",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = version.body ?: "无更新说明",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        icon = {
            Icon(
                Icons.Default.SystemUpdateAlt,
                contentDescription = "Update",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    val apkUrl = version.assets.find { it.name.endsWith(".apk") }?.browserDownloadUrl
                        ?: version.htmlUrl

                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(apkUrl))
                    context.startActivity(intent)
                    onDismiss()
                }
            ) {
                Text("立即更新")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("暂不")
            }
        }
    )
}

private fun getAppVersionName(context: Context): String {
    return try {
        val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        packageInfo.versionName ?: "1.0.0"
    } catch (e: Exception) {
        "1.0.0"
    }
}