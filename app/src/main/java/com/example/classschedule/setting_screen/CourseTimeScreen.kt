import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classschedule.data.schedule.Schedule


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScheduleScreen(initialData: List<Schedule> = emptyList()) {
    val scheduleList = remember {
        mutableStateListOf<Schedule>().apply {
            if (initialData.isNotEmpty()) addAll(initialData)
            else repeat(50) { i -> add(Schedule(courseNumber = i + 1, startTime = "00:00", endTime = "00:00")) }
        }
    }

    var showPicker by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }
    var isEditingStart by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("编辑时间表", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = { }) { Icon(Icons.Default.ArrowBack, null) } },
                actions = { TextButton(onClick = { /* 保存 */ }) { Text("保存") } }
            )
        },
        containerColor = Color(0xFFF7F8FA)
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).fillMaxSize(),
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
                        text = "要用多少节就调整多少节的时间，多余的节数忽略即可。如果想修改课表显示的节数，请去设置课表数据中的「一天课程节数」",
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

            itemsIndexed(scheduleList) { index, item ->
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
                scheduleList[editingIndex] = if (isEditingStart) old.copy(startTime = timeStr) else old.copy(endTime = timeStr)
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
    val state = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute, is24Hour = true)

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
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
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
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
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
        modifier = Modifier.width(95.dp).height(40.dp)
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