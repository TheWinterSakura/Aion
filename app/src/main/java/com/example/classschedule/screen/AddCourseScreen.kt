package com.example.classschedule.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
fun AddCourse(
    navigationUp: () -> Unit,
    viewModel: AddCourseViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    var courseName by remember { mutableStateOf("") }
    var startWeekDate by remember { mutableStateOf("") }
    var endWeekDate by remember { mutableStateOf("") }
    var weekDay by remember { mutableStateOf("") }
    var courseTime by remember { mutableStateOf("") }
    var courseCampus by remember { mutableStateOf("") }
    var courseLocation by remember { mutableStateOf("") }
    var courseTeacher by remember { mutableStateOf("") }
    var courseTeachingClass by remember { mutableStateOf("") }
    var courseTeachingClassComposition by remember { mutableStateOf("") }
    var courseAssessmentMethods by remember { mutableStateOf("") }
    var courseSelectionNotes by remember { mutableStateOf("") }
    var courseHourComposition by remember { mutableStateOf("") }
    var courseWeekStudyHours by remember { mutableStateOf("") }
    var courseTotalStudyHours by remember { mutableStateOf("") }
    var courseCredit by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val week = listOf(
        "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "添加课程") },
                navigationIcon = {
                    IconButton(onClick = { navigationUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val newCourse = Course(
                        weekDay = weekDay,
                        startWeekDate = startWeekDate.toIntOrNull() ?: 0,
                        endWeekDate = endWeekDate.toIntOrNull() ?: 0,
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
                    viewModel.addCourse(
                        course = newCourse,
                        context = context,
                        navigationUp = { navigationUp() }
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(text = "保存课程", style = MaterialTheme.typography.titleMedium)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            AddCourseItem(
                label = "课程名称",
                value = courseName,
                onValueChange = { courseName = it }
            )
            AddCourseItem(
                label = "课程起始周 (数字)",
                value = startWeekDate,
                onValueChange = { startWeekDate = it },
                keyboardType = KeyboardType.Number
            )
            AddCourseItem(
                label = "课程结束周 (数字)",
                value = endWeekDate,
                onValueChange = { endWeekDate = it },
                keyboardType = KeyboardType.Number
            )

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

            AddCourseItem(
                label = "课程时间",
                value = courseTime,
                onValueChange = { courseTime = it }
            )
            AddCourseItem(
                label = "课程校区",
                value = courseCampus,
                onValueChange = { courseCampus = it }
            )
            AddCourseItem(
                label = "课程地点",
                value = courseLocation,
                onValueChange = { courseLocation = it }
            )
            AddCourseItem(
                label = "课程教师",
                value = courseTeacher,
                onValueChange = { courseTeacher = it }
            )
            AddCourseItem(
                label = "教学班",
                value = courseTeachingClass,
                onValueChange = { courseTeachingClass = it }
            )
            AddCourseItem(
                label = "教学班组成",
                value = courseTeachingClassComposition,
                onValueChange = { courseTeachingClassComposition = it }
            )
            AddCourseItem(
                label = "考核方式",
                value = courseAssessmentMethods,
                onValueChange = { courseAssessmentMethods = it }
            )
            AddCourseItem(
                label = "选课备注",
                value = courseSelectionNotes,
                onValueChange = { courseSelectionNotes = it }
            )
            AddCourseItem(
                label = "课程学时组成",
                value = courseHourComposition,
                onValueChange = { courseHourComposition = it }
            )
            AddCourseItem(
                label = "周学时",
                value = courseWeekStudyHours,
                onValueChange = { courseWeekStudyHours = it },
                keyboardType = KeyboardType.Number
            )
            AddCourseItem(
                label = "总学时",
                value = courseTotalStudyHours,
                onValueChange = { courseTotalStudyHours = it },
                keyboardType = KeyboardType.Number
            )
            AddCourseItem(
                label = "学分",
                value = courseCredit,
                onValueChange = { courseCredit = it },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done // 最后一个输入框，键盘显示"完成"
            )

            // 底部加一点留白，防止被 BottomBar 遮挡
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AddCourseItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier, // 暴露 Modifier 以便更好的复用
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    // 优化点：移除了外层毫无意义的 Row 嵌套，直接渲染 OutlinedTextField 性能更好
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(), // 直接充满宽度
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