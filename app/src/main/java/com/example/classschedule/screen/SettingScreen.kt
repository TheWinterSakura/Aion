package com.example.classschedule.screen

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

    var newStartDate by remember { mutableStateOf("") }
    var newAllWeek by remember { mutableStateOf("") }

    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var showUniversityDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadSchool(context = context)
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
                }) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("取消")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showUniversityDialog) {
        UniversityDialog(
            universityList = universityList,
            onDismissRequest = { showUniversityDialog = false },
            onClick = { university ->
                viewModel.saveUniversity(universityUrl = university.schoolUrl)
                showUniversityDialog = false
            }
        )
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

        SettingsCard(title = "学期设置") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top, // 顶部对齐以适应 supportingText
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = newStartDate,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("修改开学日期") },
                        placeholder = { Text("请选择日期") },
                        supportingText = { Text("当前开学日期：${startDate.ifEmpty { "未设置" }}") },
                        trailingIcon = { Icon(Icons.Default.DateRange, "选择日期") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
                    )
                }

                IconButton(
                    onClick = { if (newStartDate.isNotBlank()) viewModel.saveCommencementDate(newStartDate) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Check, "保存开学日期", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = newAllWeek,
                    onValueChange = { newAllWeek = it },
                    label = { Text("修改本学期总周数") },
                    supportingText = { Text("当前总周数：${allWeek.ifEmpty { "未设置" }}") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = { if (newAllWeek.isNotBlank()) viewModel.saveAllWeek(newAllWeek) },
                    modifier = Modifier.padding(top = 8.dp),
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Icon(Icons.Default.Check, "保存总周数", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
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
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
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

        SettingsCard(title = "数据管理") {
            Button(
                onClick = { viewModel.deleteAll() },
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
                modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally)
            )
        }

        Spacer(modifier = Modifier.height(24.dp)) // 底部留白
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
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
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
        if (searchQuery.isBlank()) {
            universityList
        } else {
            universityList.filter { university ->
                university.schoolName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 600.dp),
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp),
                    placeholder = { Text("搜索学校名称...") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (filteredList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "未找到匹配的学校",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f, fill = false)
                    ) {
                        items(
                            items = filteredList,
                            key = { item -> item.schoolUrl }
                        ) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick(item) }
                                    .padding(horizontal = 24.dp, vertical = 16.dp)
                            ) {
                                Text(
                                    text = item.schoolName,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}