package com.example.classschedule.setting_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.DocumentScanner
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.IosShare
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.setting_viewmodel.SettingHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingHome(
    navigateToSchoolDate: () -> Unit,
    navigateToClassImportByLoad: () -> Unit,
    navigateToDataManager: () -> Unit,
    navigateToClassImportByPDF: () -> Unit,
    navigateToLayoutManager: () -> Unit,
    navigateToAppDetail: () -> Unit,
    navigateUp: () -> Unit,
    navigateToCourseTimeScreen: (Int) -> Unit,
    navigateToExportClassSchedule: () -> Unit,
    navigateToExportClassScheduleTimeScreen: () -> Unit,
    navigateToJsonScreen: () -> Unit,
    navigateToThemeColor: () -> Unit,
    navigateToCourseTableManager: () -> Unit,
    navigateToTimeTableManager: () -> Unit,
    viewModel: SettingHomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName
    val totalCourseNumber by viewModel.totalCourse.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "设置",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            SettingGroup(title = "基础设置") {
                SettingItem(
                    icon = Icons.Outlined.DateRange,
                    title = "学期时间设置",
                    onClick = navigateToSchoolDate
                )
                SettingItem(
                    icon = Icons.Outlined.Schedule,
                    title = "时间表管理",
                    onClick = navigateToTimeTableManager
                )
                SettingItem(
                    icon = Icons.Outlined.CalendarMonth,
                    title = "课程表管理",
                    isUnderline = false,
                    onClick = navigateToCourseTableManager
                )
            }

            SettingGroup(title = "导入方式") {
                SettingItem(
                    icon = Icons.Outlined.AccountBalance,
                    title = "教务系统导入",
                    onClick = navigateToClassImportByLoad
                )
                SettingItem(
                    icon = Icons.Outlined.DocumentScanner,
                    title = "智能图片/PDF 导入",
                    onClick = navigateToClassImportByPDF
                )
                SettingItem(
                    icon = Icons.Outlined.FolderOpen,
                    title = "分享文件导入",
                    onClick = navigateToJsonScreen,
                    isUnderline = false
                )
            }

            SettingGroup(title = "导出数据") {
                SettingItem(
                    icon = Icons.Outlined.IosShare,
                    title = "导出课程数据",
                    onClick = navigateToExportClassSchedule
                )
                SettingItem(
                    icon = Icons.Outlined.IosShare,
                    title = "导出课程时间数据",
                    isUnderline = false,
                    onClick = navigateToExportClassScheduleTimeScreen
                )
            }

            SettingGroup(title = "数据与外观") {
                SettingItem(
                    icon = Icons.Outlined.Storage,
                    title = "数据备份与管理",
                    onClick = navigateToDataManager
                )
                SettingItem(
                    icon = Icons.Outlined.Dashboard,
                    title = "界面布局管理",
                    onClick = navigateToLayoutManager
                )
                SettingItem(
                    icon = Icons.Outlined.Palette,
                    title = "主题色",
                    isUnderline = false,
                    onClick = navigateToThemeColor
                )
            }

            SettingGroup(title = "关于") {
                SettingItem(
                    icon = Icons.Outlined.Info,
                    title = "软件信息",
                    endText = "v$versionName",
                    isUnderline = false,
                    onClick = navigateToAppDetail
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingGroup(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 12.dp, bottom = 8.dp),
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.clip(RoundedCornerShape(16.dp))) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    isUnderline: Boolean = true,
    endText: String = "",
    onClick: () -> Unit,
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )

            if (endText.isNotEmpty()) {
                Text(
                    text = endText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }

        if (isUnderline) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 68.dp, end = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        }
    }
}