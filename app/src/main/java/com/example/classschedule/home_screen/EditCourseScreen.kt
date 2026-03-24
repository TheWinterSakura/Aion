package com.example.classschedule.home_screen

import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.Course

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
                title = { Text(text = "修改课程") },
                navigationIcon = {
                    IconButton(onClick = navigationUp) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (course == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
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
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCourseForm(
    modifier: Modifier = Modifier,
    initialCourse: Course,
    onSaveClick: (Course) -> Unit
) {
    var courseName by remember { mutableStateOf(initialCourse.courseName) }
    var startWeekDate by remember { mutableIntStateOf(initialCourse.startWeekDate) }
    var endWeekDate by remember { mutableIntStateOf(initialCourse.endWeekDate) }
    var weekDay by remember { mutableStateOf(initialCourse.weekDay) }
    var courseTime by remember { mutableStateOf(initialCourse.courseTime) }
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
    var expanded by remember { mutableStateOf(false) }

    val week = listOf(
        "Monday",
        "Tuesday",
        "Wednesday",
        "Thursday",
        "Friday",
        "Saturday",
        "Sunday"
    )

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AddCourseItemEdit(label = "课程名称", value = courseName, onValueChange = { courseName = it })
            AddCourseItemEdit(label = "课程起始周", value = startWeekDate.toString(), onValueChange = { startWeekDate = it.toInt() })
            AddCourseItemEdit(label = "课程结束周", value = endWeekDate.toString(), onValueChange = { endWeekDate = it.toInt() })
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = it }
            ) {
                OutlinedTextField(
                    value = weekDay.ifEmpty { "请选择课程在星期几" },
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("星期") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    week.forEach { weekDayChoose ->
                        DropdownMenuItem(
                            text = { Text(text = weekDayChoose) },
                            onClick = {
                                weekDay = weekDayChoose
                                expanded = false
                            }
                        )
                    }
                }
            }
            AddCourseItemEdit(label = "课程时间", value = courseTime, onValueChange = { courseTime = it })
            AddCourseItemEdit(label = "课程校区", value = courseCampus, onValueChange = { courseCampus = it })
            AddCourseItemEdit(label = "课程地点", value = courseLocation, onValueChange = { courseLocation = it })
            AddCourseItemEdit(label = "课程教师", value = courseTeacher, onValueChange = { courseTeacher = it })
            AddCourseItemEdit(label = "教学班", value = courseTeachingClass, onValueChange = { courseTeachingClass = it })
            AddCourseItemEdit(label = "教学班组成", value = courseTeachingClassComposition, onValueChange = { courseTeachingClassComposition = it })
            AddCourseItemEdit(label = "考核方式", value = courseAssessmentMethods, onValueChange = { courseAssessmentMethods = it })
            AddCourseItemEdit(label = "选课备注", value = courseSelectionNotes, onValueChange = { courseSelectionNotes = it })
            AddCourseItemEdit(label = "课程学时组成", value = courseHourComposition, onValueChange = { courseHourComposition = it })
            AddCourseItemEdit(label = "周学时", value = courseWeekStudyHours, onValueChange = { courseWeekStudyHours = it })
            AddCourseItemEdit(label = "总学时", value = courseTotalStudyHours, onValueChange = { courseTotalStudyHours = it })
            AddCourseItemEdit(label = "学分", value = courseCredit, onValueChange = { courseCredit = it })

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
                        startWeekDate = startWeekDate,
                        endWeekDate = endWeekDate,
                        courseName = courseName,
                        courseTime = courseTime,
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
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "保存修改", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun AddCourseItemEdit(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(top = 8.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = if (!singleLine) ImeAction.Default else imeAction
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            )
        )
    }
}