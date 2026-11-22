package com.example.classschedule.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.classschedule.data.course.Course
import com.example.classschedule.home_viewmodel.EditCourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourse(
    id: Int,
    navigationUp: () -> Unit,
    viewModel: EditCourseViewModel = hiltViewModel()
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
private fun EditCourseForm(
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
