package com.example.classschedule.setting_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.tools.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataManager(
    viewModel: DataManagerViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp:()-> Unit,
    onNavigateToHomeScreen:()-> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("数据管理") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateUp()
                        }
                    ){
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "back",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        DataManagementSection(
            onDeleteAll = { viewModel.deleteAll() },
            modifier = Modifier.padding(horizontal = 12.dp).padding(innerPadding),
            onNavigateToHomeScreen = {onNavigateToHomeScreen()}
        )
    }
}

@Composable
private fun DataManagementSection(
    onDeleteAll: () -> Unit,
    modifier: Modifier,
    onNavigateToHomeScreen:()-> Unit
) {
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("清空课程") },
            text = { Text("确定要删除所有课程数据吗？此操作不可恢复。") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteAll()
                        "删除所有数据成功".showToast()
                        showDeleteConfirmDialog = false
                        onNavigateToHomeScreen()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("确认删除") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) { Text("取消") }
            }
        )
    }

    SettingsCard(
        title = "数据管理",
        modifier = modifier,
    ) {
        Button(
            onClick = { showDeleteConfirmDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp)
            )
            Text(text = "删除所有课程")
        }
        Text(
            text = "此操作不可逆，请谨慎操作",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
        )
    }
}


@Composable
fun SettingsCard(
    title: String,
    modifier: Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            content()
        }
    }
}
