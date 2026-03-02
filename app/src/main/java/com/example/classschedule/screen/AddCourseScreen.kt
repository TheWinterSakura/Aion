package com.example.classschedule.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp


@ExperimentalMaterial3Api
@Composable
fun AddCourse(
    navigationUp: () -> Unit
) {
    var courseName by remember { mutableStateOf("") }
    var courseWeek by remember { mutableStateOf("") }
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


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "添加课程")
                },
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
                onClick = { /* TODO: 保存逻辑 */ },
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
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AddCourseItem(
                label = "课程名称",
                value = courseName,
                onValueChange = { inputText ->
                    courseName = inputText
                },
            )
            AddCourseItem(
                label = "课程持续周数",
                value = courseWeek,
                onValueChange = { inputText ->
                    courseWeek = inputText
                },
            )
            AddCourseItem(
                label = "课程时间",
                value = courseTime,
                onValueChange = { inputText ->
                    courseTime = inputText
                },
            )
            AddCourseItem(
                label = "课程校区",
                value = courseCampus,
                onValueChange = { inputText ->
                    courseCampus = inputText
                },
            )
            AddCourseItem(
                label = "课程地点",
                value = courseLocation,
                onValueChange = { inputText ->
                    courseLocation = inputText
                },
            )
            AddCourseItem(
                label = "课程教师",
                value = courseTeacher,
                onValueChange = { inputText ->
                    courseTeacher = inputText
                },
            )
            AddCourseItem(
                label = "教学班",
                value = courseTeachingClass,
                onValueChange = { inputText ->
                    courseTeachingClass = inputText
                },
            )
            AddCourseItem(
                label = "教学班组成",
                value = courseTeachingClassComposition,
                onValueChange = { inputText ->
                    courseTeachingClassComposition = inputText
                },
            )
            AddCourseItem(
                label = "考核方式",
                value = courseAssessmentMethods,
                onValueChange = { inputText ->
                    courseAssessmentMethods = inputText
                },
            )
            AddCourseItem(
                label = "选课备注",
                value = courseSelectionNotes,
                onValueChange = { inputText ->
                    courseSelectionNotes = inputText
                },
            )
            AddCourseItem(
                label = "课程学时组成",
                value = courseHourComposition,
                onValueChange = { inputText ->
                    courseHourComposition = inputText
                },
            )
            AddCourseItem(
                label = "周学时",
                value = courseWeekStudyHours,
                onValueChange = { inputText ->
                    courseWeekStudyHours = inputText
                },
            )
            AddCourseItem(
                label = "总学时",
                value = courseTotalStudyHours,
                onValueChange = { inputText ->
                    courseTotalStudyHours = inputText
                },
            )
            AddCourseItem(
                label = "学分",
                value = courseCredit,
                onValueChange = { inputText ->
                    courseCredit = inputText
                },
            )
        }
    }
}


@Composable
fun AddCourseItem(
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