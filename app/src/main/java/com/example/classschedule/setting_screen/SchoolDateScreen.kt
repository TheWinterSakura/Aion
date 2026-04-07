package com.example.classschedule.setting_screen

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog // 确保导入了 AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.setting_viewmodel.SchoolDateViewModel
import com.example.classschedule.tools.showToast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun SchoolDate(
    onNavigateBack: () -> Unit,
    viewModel: SchoolDateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val startDate by viewModel.startDate.collectAsState()
    val allWeek by viewModel.allWeek.collectAsState()
    val totalCourse by viewModel.totalCourse.collectAsState()

    Scaffold(
        topBar = {
            SchoolDataTopAppBar(onNavigateBack = onNavigateBack)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TermSettingsContent(
                startDate = startDate,
                allWeek = allWeek,
                totalCourseNumber = totalCourse,
                onSaveStartDate = { viewModel.saveCommencementDate(it) },
                onSaveAllWeek = { viewModel.saveAllWeek(it) },
                onSaveAllCourseNumber = { viewModel.saveTotalCourse(it) },
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SchoolDataTopAppBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = { Text("学期设置") },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TermSettingsContent(
    startDate: String,
    allWeek: String,
    onSaveStartDate: (String) -> Unit,
    onSaveAllWeek: (String) -> Unit,
    totalCourseNumber: Int,
    onSaveAllCourseNumber: (String) -> Unit,
    viewModel: SchoolDateViewModel
) {
    var newStartDate by remember { mutableStateOf("") }
    var newAllWeek by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    var newTotalCourse by remember { mutableStateOf("") }
    var showCourseWarningDialog by remember { mutableStateOf(false) }

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

    if (showCourseWarningDialog) {
        AlertDialog(
            onDismissRequest = { showCourseWarningDialog = false },
            title = {
                Text(text = "修改课程节数警告", color = MaterialTheme.colorScheme.error)
            },
            text = {
                Text(
                    text = "如果你已完成你的课程时间表，修改总数时，将会根据你填入的总课程数重新调整时间表，这可能会增加或删除最后几条数据。\n\n确定要修改为 $newTotalCourse 节吗？"
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showCourseWarningDialog = false
                        val newTotalCourseInt = newTotalCourse.toIntOrNull()!!

                        if (newTotalCourseInt > totalCourseNumber) {
                            onSaveAllCourseNumber(newTotalCourse)
                            for (i in (totalCourseNumber + 1)..newTotalCourseInt) {
                                viewModel.insertCourseTime(
                                    Schedule(
                                        courseNumber = i,
                                        startTime = "00:00",
                                        endTime = "00:00"
                                    )
                                )
                            }

                        } else if (newTotalCourseInt < totalCourseNumber) {
                            onSaveAllCourseNumber(newTotalCourse)
                            for (i in (newTotalCourseInt + 1)..totalCourseNumber) {
                                viewModel.deleteByCourseNumber(i)
                            }

                        } else {
                            "出现未知错误，请重试".showToast()
                        }
                        newTotalCourse = ""
                    }
                ) {
                    Text("确定修改", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showCourseWarningDialog = false }
                ) {
                    Text("取消")
                }
            }
        )
    }

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
            label = { Text("开学日期") },
            placeholder = { Text("请选择日期") },
            supportingText = { Text("当前: ${startDate.ifEmpty { "未设置" }}") },
            trailingIcon = { Icon(Icons.Default.DateRange, "选择日期") },
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                if (newStartDate.isNotBlank()) {
                    onSaveStartDate(newStartDate)
                    newStartDate = ""
                }
            },
            enabled = newStartDate.isNotBlank(),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                Icons.Default.Check,
                "保存开学日期",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = newAllWeek,
            onValueChange = { newAllWeek = it.filter { char -> char.isDigit() } },
            label = { Text("本学期总周数") },
            placeholder = { Text("例如: 20") },
            supportingText = { Text("当前: ${allWeek.ifEmpty { "未设置" }}") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                if (newAllWeek.isNotBlank()) {
                    onSaveAllWeek(newAllWeek)
                    newAllWeek = ""
                }
            },
            enabled = newAllWeek.isNotBlank(),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                Icons.Default.Check,
                "保存总周数",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = newTotalCourse,
            onValueChange = { newTotalCourse = it.filter { char -> char.isDigit() } },
            label = { Text("一天课程节数") },
            placeholder = { Text("例如: 20") },
            supportingText = {
                Text("当前: ${if (totalCourseNumber == 0) "未设置" else totalCourseNumber}")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(1f)
        )

        IconButton(
            onClick = {
                if (newTotalCourse.isNotBlank()) {
                    val newCount = newTotalCourse.toIntOrNull() ?: 0
                    if (newCount != totalCourseNumber) {
                        if (newCount > 100) {
                            "最多只能设置100节课".showToast()
                            return@IconButton
                        } else if (newCount <= 0) {
                            "课程数不能小于或等于0".showToast()
                            return@IconButton
                        }
                        showCourseWarningDialog = true
                    } else {
                        newTotalCourse = ""
                    }
                }
            },
            enabled = newTotalCourse.isNotBlank(),
            colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                Icons.Default.Check,
                "保存总节数",
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

private fun convertMillisToDateString(millis: Long): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    formatter.timeZone = TimeZone.getTimeZone("UTC")
    return formatter.format(Date(millis))
}