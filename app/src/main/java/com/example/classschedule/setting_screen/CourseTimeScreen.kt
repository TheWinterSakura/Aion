package com.example.classschedule.setting_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.tools.showToast
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(
    viewModel: CourseTimeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit,
    totalCourseNumber: Int = 20
) {
    val initialData by viewModel.scheduleList.collectAsState()
    val scheduleList = remember { mutableStateListOf<Schedule>() }
    val autoCalcEnabled by viewModel.autoCalcEnabled.collectAsState()
    var classDuration by remember { mutableStateOf("45") }

    LaunchedEffect(initialData) {
        if (initialData.isNotEmpty()) {
            scheduleList.clear()
            scheduleList.addAll(initialData)
        } else if (scheduleList.isEmpty()) {
            repeat(totalCourseNumber) { i ->
                scheduleList.add(
                    Schedule(courseNumber = i + 1, startTime = "00:00", endTime = "00:00")
                )
            }
        }
    }

    var showPicker by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var isEditingStart by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑时间表", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                ),
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    }
                },
                actions = {
                    TextButton(onClick = {
                        if (initialData.isNotEmpty()){
                            viewModel.updateAll(scheduleList = scheduleList)
                            "修改成功".showToast()
                        } else {
                            scheduleList.forEach { schedule ->
                                viewModel.insertCourseTime(schedule)
                            }
                            "添加成功".showToast()
                        }
                        navigateUp()
                    }) { Text("保存") }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp)
                ) {
                    Text(
                        text = "要用多少节就调整多少节的时间，多余的节数忽略即可。如果想修改课表显示的节数，请去设置当中的学期时间设置中的「一天课程节数」",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "请注意是 24 小时制！",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = "自动计算结束时间",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "设定开始时间后，自动算出结束时间",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = autoCalcEnabled,
                                onCheckedChange = { viewModel.changeAutoCalcEnabled(it) }
                            )
                        }

                        AnimatedVisibility(visible = autoCalcEnabled) {
                            OutlinedTextField(
                                value = classDuration,
                                onValueChange = {
                                    if (it.isEmpty() || it.matches(Regex("^\\d+$"))) {
                                        classDuration = it
                                    }
                                },
                                label = { Text("每节课时长 (分钟)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            itemsIndexed(
                items = scheduleList,
                key = { _, item -> item.courseNumber }
            ) { index, item ->
                ScheduleItemRow(
                    schedule = item,
                    onStartClick = { editingIndex = index; isEditingStart = true; showPicker = true },
                    onEndClick = { editingIndex = index; isEditingStart = false; showPicker = true }
                )
            }
        }
    }

    if (showPicker && editingIndex != -1) {
        val current = scheduleList[editingIndex]
        val (h, m) = parseTimeString(if (isEditingStart) current.startTime else current.endTime)

        EnhancedTimePickerDialog(
            initialHour = h,
            initialMinute = m,
            title = "第 ${current.courseNumber} 节${if (isEditingStart) "开始" else "结束"}时间",
            onDismiss = { showPicker = false },
            onConfirm = { hour, min ->
                val timeStr = formatTimeInt(hour, min)
                val old = scheduleList[editingIndex]

                if (isEditingStart && autoCalcEnabled) {
                    val duration = classDuration.toIntOrNull() ?: 0
                    if (duration > 0) {
                        try {
                            val startLocalTime = LocalTime.of(hour, min)
                            val endLocalTime = startLocalTime.plusMinutes(duration.toLong())
                            val endTimeStr = formatTimeInt(endLocalTime.hour, endLocalTime.minute)

                            scheduleList[editingIndex] = old.copy(
                                startTime = timeStr,
                                endTime = endTimeStr
                            )
                        } catch (e: Exception) {
                            scheduleList[editingIndex] = old.copy(startTime = timeStr)
                        }
                    } else {
                        scheduleList[editingIndex] = old.copy(startTime = timeStr)
                    }
                } else if (isEditingStart) {
                    scheduleList[editingIndex] = old.copy(startTime = timeStr)
                } else {
                    scheduleList[editingIndex] = old.copy(endTime = timeStr)
                }
                showPicker = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedTimePickerDialog(
    initialHour: Int,
    initialMinute: Int,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    val state = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = true
    )

    var showingPickerMode by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false),
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }) { Text("确定") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("取消") }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface)

                    IconButton(onClick = { showingPickerMode = !showingPickerMode }) {
                        Icon(
                            imageVector = if (showingPickerMode) Icons.Default.Keyboard else Icons.Default.Schedule,
                            contentDescription = "切换输入模式",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                if (showingPickerMode) {
                    TimePicker(state = state)
                } else {
                    TimeInput(state = state)
                }
            }
        }
    )
}

@Composable
fun ScheduleItemRow(schedule: Schedule, onStartClick: () -> Unit, onEndClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "第 ${schedule.courseNumber} 节",
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        TimeCapsule(text = schedule.startTime, onClick = onStartClick)
        Text(
            text = "-",
            modifier = Modifier.padding(horizontal = 8.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
        TimeCapsule(text = schedule.endTime, onClick = onEndClick)
    }
}

@Composable
fun TimeCapsule(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 0.dp,
        modifier = Modifier
            .width(95.dp)
            .height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

fun parseTimeString(time: String): Pair<Int, Int> {
    return try {
        val parts = time.split(":")
        parts[0].toInt() to parts[1].toInt()
    } catch (e: Exception) {
        8 to 0
    }
}

fun formatTimeInt(hour: Int, minute: Int): String {
    return "%02d:%02d".format(hour, minute)
}