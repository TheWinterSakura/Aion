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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.setting_screen.CourseTimeViewModel
import com.example.classschedule.tools.showToast


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(
    viewModel: CourseTimeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit,
    totalCourseNumber: Int = 20
) {
    val initialData by viewModel.scheduleList.collectAsState()

    val scheduleList = remember { mutableStateListOf<Schedule>() }

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
                navigationIcon = {
                    IconButton(onClick = {
                        navigateUp()
                    }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, null) }
                },
                actions = {
                    TextButton(onClick = {
                        if (initialData.isNotEmpty()){
                            viewModel.updateAll(scheduleList = scheduleList)
                            "修改成功".showToast()
                        }else{
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
        containerColor = Color(0xFFF7F8FA)
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
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        text = "要用多少节就调整多少节的时间，多余的节数忽略即可。如果想修改课表显示的节数，请去设置当中的学期时间设置中的「一天课程节数」",
                        fontSize = 14.sp,
                        color = Color(0xFF666666),
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "请注意是 24 小时制！",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
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
                scheduleList[editingIndex] =
                    if (isEditingStart) old.copy(startTime = timeStr) else old.copy(endTime = timeStr)
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
        properties = androidx.compose.ui.window.DialogProperties(usePlatformDefaultWidth = false), // 让弹窗宽度自适应
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
                    Text(title, style = MaterialTheme.typography.labelLarge)

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
        Text("第 ${schedule.courseNumber} 节", modifier = Modifier.weight(1f), fontSize = 16.sp)

        TimeCapsule(text = schedule.startTime, onClick = onStartClick)
        Text("-", modifier = Modifier.padding(horizontal = 8.dp), color = Color.LightGray)
        TimeCapsule(text = schedule.endTime, onClick = onEndClick)
    }
}

@Composable
fun TimeCapsule(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        tonalElevation = 1.dp,
        modifier = Modifier
            .width(95.dp)
            .height(40.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 15.sp, fontWeight = FontWeight.Medium)
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