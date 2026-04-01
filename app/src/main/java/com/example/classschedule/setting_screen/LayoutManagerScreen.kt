package com.example.classschedule.setting_screen

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LayoutManager(
    onBack: () -> Unit = {},
    viewModel: LayOutManagerViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isGridLayout by viewModel.isGridLayout.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("布局管理") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {

            LayoutManagementSection(
                isGridLayout = isGridLayout,
                onLayoutChange = { viewModel.saveIsGridLayout(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            WidgetGuideCard()
        }
    }
}

@Composable
private fun LayoutManagementSection(
    isGridLayout: Boolean,
    onLayoutChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    SettingsCard(
        title = "布局设置",
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = LocalIndication.current
                ) { onLayoutChange(!isGridLayout) }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 12.dp)
            ) {
                Text(
                    text = "网格布局",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = if (isGridLayout) "当前：网格布局" else "当前：滑动布局",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Switch(
                checked = isGridLayout,
                onCheckedChange = onLayoutChange
            )
        }
    }
}

@Composable
fun WidgetGuideCard() {
    SettingsCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        title = "将课表添加到桌面"
    ) {
        Text(
            "无需打开 App，抬手就能看到下一节课,长按桌面找到app小组件即可",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}