package com.example.classschedule.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.course.Course

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourse(
    id: Int,
    navigationUp: () -> Unit,
    viewModel: EditCourseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val context = LocalContext.current
    LaunchedEffect(id) {
        viewModel.loadCourse(id = id)
    }

    val course by viewModel._course.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "修改课程", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigationUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (course == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            EditCourseForm(
                modifier = Modifier.padding(innerPadding),
                initialCourse = course!!,
                onSaveClick = { updatedCourse ->
                    viewModel.changeCourse(
                        course = updatedCourse,
                        context = context,
                        navigationUp = navigationUp
                    )
                },
                viewModel = viewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseForm(
    modifier: Modifier = Modifier,
    initialCourse: Course,
    onSaveClick: (Course) -> Unit,
    viewModel: EditCourseViewModel
) {
    var courseName by remember { mutableStateOf(initialCourse.courseName) }
    var startWeekDate by remember { mutableStateOf(initialCourse.startWeekDate.toString()) }
    var endWeekDate by remember { mutableStateOf(initialCourse.endWeekDate.toString()) }
    var weekDay by remember { mutableStateOf(initialCourse.weekDay) }

    var showPeriodDialog by remember { mutableStateOf(false) }
    var startPeriod by remember { mutableIntStateOf(1) }
    var endPeriod by remember { mutableIntStateOf(2) }

    var courseCampus by remember { mutableStateOf(initialCourse.courseCampus) }
    var courseLocation by remember { mutableStateOf(initialCourse.courseLocation) }
    var courseTeacher by remember { mutableStateOf(initialCourse.courseTeacher) }
    var courseTeachingClass by remember { mutableStateOf(initialCourse.courseTeachingClass) }
    var courseTeachingClassComposition by remember { mutableStateOf(initialCourse.courseTeachingClassComposition) }
    var courseAssessmentMethods by remember { mutableStateOf(initialCourse.courseAssessmentMethods) }
    var courseSelectionNotes by remember { mutableStateOf(initialCourse.courseSelectionNotes) }
    var courseHourComposition by remember { mutableStateOf(initialCourse.courseHourComposition) }
    var courseWeekStudyHours by remember { mutableStateOf(initialCourse.courseWeekStudyHours) }
    var courseTotalStudyHours by remember { mutableStateOf(initialCourse.courseTotalStudyHours) }
    var courseCredit by remember { mutableStateOf(initialCourse.courseCredit) }
    val maxPeriodsPerDay by viewModel.maxPeriodsPerDay.collectAsState()


    var weekExpanded by remember { mutableStateOf(false) }
    val weekList =
        listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    LaunchedEffect(initialCourse.courseTime) {
        val regex = Regex("\\((\\d+)-(\\d+)节\\)")
        val match = regex.find(initialCourse.courseTime)
        if (match != null) {
            startPeriod = match.groupValues[1].toIntOrNull() ?: 1
            endPeriod = match.groupValues[2].toIntOrNull() ?: 2
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            FormCard(title = "基本信息") {
                AddCourseItemEdit(
                    label = "课程名称",
                    value = courseName,
                    onValueChange = { courseName = it })
                AddCourseItemEdit(
                    label = "课程教师",
                    value = courseTeacher,
                    onValueChange = { courseTeacher = it })
                AddCourseItemEdit(
                    label = "课程校区",
                    value = courseCampus,
                    onValueChange = { courseCampus = it })
                AddCourseItemEdit(
                    label = "课程地点",
                    value = courseLocation,
                    onValueChange = { courseLocation = it })
            }

            FormCard(title = "时间安排") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AddCourseItemEdit(
                        modifier = Modifier.weight(1f),
                        label = "起始周",
                        value = startWeekDate,
                        onValueChange = { startWeekDate = it },
                        keyboardType = KeyboardType.Number
                    )
                    AddCourseItemEdit(
                        modifier = Modifier.weight(1f),
                        label = "结束周",
                        value = endWeekDate,
                        onValueChange = { endWeekDate = it },
                        keyboardType = KeyboardType.Number
                    )
                }

                ExposedDropdownMenuBox(
                    expanded = weekExpanded,
                    onExpandedChange = { weekExpanded = it },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    OutlinedTextField(
                        value = weekDay.ifEmpty { "请选择星期" },
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("星期") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weekExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors()
                    )
                    ExposedDropdownMenu(
                        expanded = weekExpanded,
                        onDismissRequest = { weekExpanded = false }
                    ) {
                        weekList.forEach { weekDayChoose ->
                            DropdownMenuItem(
                                text = { Text(text = weekDayChoose) },
                                onClick = {
                                    weekDay = weekDayChoose
                                    weekExpanded = false
                                }
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showPeriodDialog = true }
                ) {
                    OutlinedTextField(
                        value = "$startPeriod-${endPeriod}节",
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        label = { Text("课程节数") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline,
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            FormCard(title = "教学班信息") {
                AddCourseItemEdit(
                    label = "教学班",
                    value = courseTeachingClass,
                    onValueChange = { courseTeachingClass = it })
                AddCourseItemEdit(
                    label = "教学班组成",
                    value = courseTeachingClassComposition,
                    onValueChange = { courseTeachingClassComposition = it })
                AddCourseItemEdit(
                    label = "考核方式",
                    value = courseAssessmentMethods,
                    onValueChange = { courseAssessmentMethods = it })
            }

            FormCard(title = "学时与学分") {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AddCourseItemEdit(
                        modifier = Modifier.weight(1f),
                        label = "学分",
                        value = courseCredit,
                        onValueChange = { courseCredit = it },
                        keyboardType = KeyboardType.Number
                    )
                    AddCourseItemEdit(
                        modifier = Modifier.weight(1f),
                        label = "周学时",
                        value = courseWeekStudyHours,
                        onValueChange = { courseWeekStudyHours = it },
                        keyboardType = KeyboardType.Number
                    )
                    AddCourseItemEdit(
                        modifier = Modifier.weight(1f),
                        label = "总学时",
                        value = courseTotalStudyHours,
                        onValueChange = { courseTotalStudyHours = it },
                        keyboardType = KeyboardType.Number
                    )
                }
                AddCourseItemEdit(
                    label = "课程学时组成",
                    value = courseHourComposition,
                    onValueChange = { courseHourComposition = it })
                AddCourseItemEdit(
                    label = "选课备注",
                    value = courseSelectionNotes,
                    onValueChange = { courseSelectionNotes = it })
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        Surface(
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {
            Button(
                onClick = {
                    val newCourse = Course(
                        id = initialCourse.id,
                        weekDay = weekDay,
                        startWeekDate = startWeekDate.toIntOrNull() ?: 0,
                        endWeekDate = endWeekDate.toIntOrNull() ?: 0,
                        courseName = courseName,
                        courseTime = "($startPeriod-${endPeriod}节)",
                        courseCampus = courseCampus,
                        courseLocation = courseLocation,
                        courseTeacher = courseTeacher,
                        courseTeachingClass = courseTeachingClass,
                        courseTeachingClassComposition = courseTeachingClassComposition,
                        courseAssessmentMethods = courseAssessmentMethods,
                        courseSelectionNotes = courseSelectionNotes,
                        courseHourComposition = courseHourComposition,
                        courseWeekStudyHours = courseWeekStudyHours,
                        courseTotalStudyHours = courseTotalStudyHours,
                        courseCredit = courseCredit
                    )
                    onSaveClick(newCourse)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text(text = "保存修改", style = MaterialTheme.typography.titleMedium)
            }
        }
    }

    if (showPeriodDialog) {
        PeriodSelectionDialog(
            maxPeriods = maxPeriodsPerDay,
            initialStart = startPeriod,
            initialEnd = endPeriod,
            onDismiss = { showPeriodDialog = false },
            onConfirm = { start, end ->
                startPeriod = start
                endPeriod = end
                showPeriodDialog = false
            }
        )
    }
}

@Composable
fun FormCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                alpha = 0.5f
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}


@Composable
fun AddCourseItemEdit(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        shape = MaterialTheme.shapes.medium,
        singleLine = singleLine,
        minLines = minLines,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = if (!singleLine) ImeAction.Default else imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        )
    )
}


@Composable
fun PeriodSelectionDialog(
    maxPeriods: Int,
    initialStart: Int,
    initialEnd: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var tempStart by remember { mutableIntStateOf(initialStart) }
    var tempEnd by remember { mutableIntStateOf(initialEnd) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "选择课程节数", fontWeight = FontWeight.Bold) },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "起始节",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items((1..maxPeriods).toList()) { period ->
                            val isSelected = tempStart == period
                            Text(
                                text = "第 $period 节",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, shape = MaterialTheme.shapes.small)
                                    .clickable {
                                        tempStart = period
                                        if (tempEnd < tempStart) tempEnd = tempStart
                                    }
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Text(
                    "至",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "结束节",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items((1..maxPeriods).toList()) { period ->
                            val isSelected = tempEnd == period
                            Text(
                                text = "第 $period 节",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent, shape = MaterialTheme.shapes.small)
                                    .clickable {
                                        if (period >= tempStart) {
                                            tempEnd = period
                                        }
                                    }
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                color = if (period < tempStart) Color.Gray.copy(alpha = 0.5f)
                                else if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempStart, tempEnd) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}