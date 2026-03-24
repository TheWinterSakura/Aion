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
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingHome(
    navigateToSchoolDate: () -> Unit,
    navigateToClassImportByLoad: () -> Unit,
    navigateToDataManager: () -> Unit,
    navigateToClassImportByPDF: () -> Unit,
    navigateToLayoutManager: () -> Unit,
    navigateToAppDetail: () -> Unit,
    navigateUp:()-> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = navigateUp
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = "back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
    ) { paddingValues ->

        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SettingGroup(title = "常规") {
                SettingItem(
                    icon = Icons.Outlined.DateRange,
                    title = "学期设置",
                    isUnderline = false,
                    onClick = navigateToSchoolDate
                )
            }

            SettingGroup(title = "数据获取") {
                SettingItem(
                    icon = Icons.Outlined.CloudDownload,
                    title = "教务系统课程导入",
                    onClick = navigateToClassImportByLoad
                )
                SettingItem(
                    icon = Icons.Outlined.Image,
                    title = "图片课程导入",
                    isUnderline = false,
                    onClick = navigateToClassImportByPDF
                )
            }

            SettingGroup(title = "高级与个性化") {
                SettingItem(
                    icon = Icons.Outlined.Storage,
                    title = "数据管理",
                    onClick = navigateToDataManager
                )
                SettingItem(
                    icon = Icons.Outlined.Dashboard,
                    title = "布局管理",
                    isUnderline = false,
                    onClick = navigateToLayoutManager
                )
            }

            SettingGroup(title = "其他") {
                SettingItem(
                    icon = Icons.Outlined.Info,
                    title = "关于 App",
                    endText = "v1.0.0",
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
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp
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
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                if (endText.isNotEmpty()) {
                    Text(
                        text = endText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "前往详细设置",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (isUnderline) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 56.dp, end = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}