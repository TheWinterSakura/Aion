package com.example.classschedule.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.school.University
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetScreen(
    viewModel: SettingViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToWeb: (String) -> Unit,
) {
    val startDate by viewModel.startDate.collectAsState()
    val allWeek by viewModel.allWeek.collectAsState()
    val universityList by viewModel.universityList.collectAsState()
    val universityUrl by viewModel.universityUrl.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadSchool(context = context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "设置",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
        )

        TermSettingsSection(
            startDate = startDate,
            allWeek = allWeek,
            onSaveStartDate = { viewModel.saveCommencementDate(it) },
            onSaveAllWeek = { viewModel.saveAllWeek(it) }
        )

        ImportSettingsSection(
            universityUrl = universityUrl,
            universityList = universityList,
            onUniversitySelected = { viewModel.saveUniversity(universityUrl = it.schoolUrl) },
            onNavigateToWeb = onNavigateToWeb
        )

        DataManagementSection(
            onDeleteAll = { viewModel.deleteAll() }
        )

        LayoutManagementSection(
            isGridLayout = isGridLayout,
            onLayoutChange = { viewModel.saveIsGridLayout(it) }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermSettingsSection(
    startDate: String,
    allWeek: String,
    onSaveStartDate: (String) -> Unit,
    onSaveAllWeek: (String) -> Unit
) {
    var newStartDate by remember { mutableStateOf("") }
    var newAllWeek by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val dateInteractionSource = remember { MutableInteractionSource() }
    if (dateInteractionSource.collectIsPressedAsState().value) {
        showDatePicker = true
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        newStartDate = convertMillisToDateString(millis)
                    }
                    showDatePicker = false
                }) { Text("确定") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("取消") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    SettingsCard(title = "学期设置") {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newStartDate,
                onValueChange = {},
                readOnly = true,
                interactionSource = dateInteractionSource,
                label = { Text("修改开学日期") },
                placeholder = { Text("当前：${startDate.ifEmpty { "未设置" }}") },
                trailingIcon = { Icon(Icons.Default.DateRange, "选择日期") },
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { if (newStartDate.isNotBlank()) onSaveStartDate(newStartDate) },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(Icons.Default.Check, "保存开学日期", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Text("当前开学日期：${startDate.ifEmpty { "未设置" }}", fontSize = 12.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newAllWeek,
                onValueChange = { newAllWeek = it },
                label = { Text("修改本学期总周数") },
                placeholder = { Text("当前：${allWeek.ifEmpty { "未设置" }}") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { if (newAllWeek.isNotBlank()) onSaveAllWeek(newAllWeek) },
                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Icon(Icons.Default.Check, "保存总周数", tint = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Text("当前学期总周数：${allWeek.ifEmpty { "未设置" }}", fontSize = 12.sp)
    }
}

@Composable
private fun ImportSettingsSection(
    universityUrl: String,
    universityList: List<University>,
    onUniversitySelected: (University) -> Unit,
    onNavigateToWeb: (String) -> Unit
) {
    var showUniversityDialog by remember { mutableStateOf(false) }

    if (showUniversityDialog) {
        UniversityDialog(
            universityList = universityList,
            onDismissRequest = { showUniversityDialog = false },
            onClick = { university ->
                onUniversitySelected(university)
                showUniversityDialog = false
            }
        )
    }

    SettingsCard(title = "教务系统导入") {
        OutlinedButton(
            onClick = { showUniversityDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("选择学校")
        }

        if (universityUrl.isNotBlank()) {
            Text(
                text = "当前网址: $universityUrl",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = { onNavigateToWeb(universityUrl) },
            modifier = Modifier.fillMaxWidth(),
            enabled = universityUrl.isNotBlank()
        ) {
            Text(text = "登录教务系统导入课程表")
        }
    }
}

@Composable
private fun DataManagementSection(
    onDeleteAll: () -> Unit
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
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("确认删除") }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) { Text("取消") }
            }
        )
    }

    SettingsCard(title = "数据管理") {
        Button(
            onClick = { showDeleteConfirmDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.padding(end = 8.dp).size(18.dp)
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
private fun LayoutManagementSection(
    isGridLayout: Boolean,
    onLayoutChange: (Boolean) -> Unit
) {
    SettingsCard(title = "布局管理") {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onLayoutChange(!isGridLayout) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "是否开启网格布局",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "当前布局为: ${if (isGridLayout) "网格布局" else "滑动布局"}",
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
fun SettingsCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
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

private fun convertMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}

@Composable
fun UniversityDialog(
    universityList: List<University>,
    onDismissRequest: () -> Unit,
    onClick: (University) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredList = remember(searchQuery, universityList) {
        if (searchQuery.isBlank()) universityList
        else universityList.filter { it.schoolName.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(max = 600.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "选择学校",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    placeholder = { Text("搜索学校名称...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (filteredList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("未找到匹配的学校", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        items(items = filteredList, key = { it.schoolUrl }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick(item) }
                                    .padding(horizontal = 24.dp, vertical = 16.dp)
                            ) {
                                Text(text = item.schoolName, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}