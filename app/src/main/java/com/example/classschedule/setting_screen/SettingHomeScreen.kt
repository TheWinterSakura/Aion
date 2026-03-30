package com.example.classschedule.setting_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    navigateToCourseTimeScreen:()-> Unit
) {
    val context = LocalContext.current
    val versionName = context.packageManager.getPackageInfo(context.packageName, 0).versionName

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "设置",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, "back")
                    }
                }
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            SettingGroup(title = "基础设置") {
                SettingItem(
                    icon = Icons.Outlined.DateRange,
                    title = "学期时间设置",
                    onClick = navigateToSchoolDate
                )
                SettingItem(
                    icon = Icons.Outlined.DateRange,
                    title = "课程时间表设置",
                    onClick = navigateToCourseTimeScreen
                )
            }

            SettingGroup(title = "导入方式") {
                SettingItem(
                    icon = Icons.Outlined.CloudDownload,
                    title = "教务系统导入",
                    onClick = navigateToClassImportByLoad
                )
                SettingItem(
                    icon = Icons.Outlined.AutoAwesome,
                    title = "智能图片/PDF 导入",
                    onClick = navigateToClassImportByPDF
                )
            }

            SettingGroup(title = "数据与外观") {
                SettingItem(
                    icon = Icons.Outlined.Storage,
                    title = "数据备份与管理",
                    onClick = navigateToDataManager
                )
                SettingItem(
                    icon = Icons.Outlined.Palette,
                    title = "界面布局管理",
                    onClick = navigateToLayoutManager
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

            Spacer(modifier = Modifier.height(24.dp))
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
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
            letterSpacing = 0.5.sp
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF8F8F8)
        ) {
            Column {
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF222222),
            modifier = Modifier
                .weight(1f)
                .padding(start = 14.dp)
        )

        if (endText.isNotEmpty()) {
            Text(
                text = endText,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(end = 4.dp)
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color(0xFFCCCCCC),
            modifier = Modifier.size(20.dp)
        )
    }

    if (isUnderline) {
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 0.5.dp,
            color = Color(0xFFEEEEEE)
        )
    }
}