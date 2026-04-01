package com.example.classschedule.home_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.course.Course

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
    var courseTimeStart by remember { mutableStateOf("") }
    var courseTimeEnd by remember { mutableStateOf("") }
    var courseCampus by remember { mutableStateOf("") }
    var courseLocation by remember { mutableStateOf("") }

    // 选填
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

    val week = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val isFormValid = courseName.isNotBlank() &&
            startWeekDate.isNotBlank() &&
            endWeekDate.isNotBlank() &&
            weekDay.isNotBlank() &&
            courseTimeStart.isNotBlank() &&
            courseCampus.isNotBlank() &&
            courseLocation.isNotBlank() &&
            courseTimeEnd.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "添加课程", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = navigationUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回"
                        )
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = {
                        val newCourse = Course(
                            weekDay = weekDay,
                            startWeekDate = startWeekDate.toIntOrNull() ?: 0,
                            endWeekDate = endWeekDate.toIntOrNull() ?: 0,
                            courseName = courseName,
                            courseTime = ("$courseTimeStart-${courseTimeEnd}节"),
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
                            navigationUp = navigationUp
                        )
                    },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(54.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isFormValid) "保存课程" else "请完善必填信息",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SectionTitle(icon = Icons.Rounded.Info, title = "基础信息")
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AddCourseItem(
                        label = "课程名称 *",
                        value = courseName,
                        leadingIcon = Icons.Rounded.Edit,
                        onValueChange = { courseName = it }
                    )
                    AddCourseItem(
                        label = "课程教师 (选填)",
                        value = courseTeacher,
                        leadingIcon = Icons.Rounded.Person,
                        onValueChange = { courseTeacher = it }
                    )
                }
            }

            SectionTitle(icon = Icons.Rounded.DateRange, title = "时间安排")
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AddCourseItem(
                            modifier = Modifier.weight(1f),
                            label = "起始周 *",
                            value = startWeekDate,
                            leadingIcon = Icons.Rounded.PlayArrow,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) startWeekDate = it },
                            keyboardType = KeyboardType.Number
                        )
                        AddCourseItem(
                            modifier = Modifier.weight(1f),
                            label = "结束周 *",
                            value = endWeekDate,
                            leadingIcon = Icons.Rounded.Close,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) endWeekDate = it },
                            keyboardType = KeyboardType.Number
                        )
                    }

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = it }
                    ) {
                        OutlinedTextField(
                            value = weekDay.ifEmpty { "请选择课程在星期几 *" },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("星期 *") },
                            leadingIcon = {
                                Icon(Icons.Rounded.DateRange, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = if (weekDay.isEmpty()) Color.Gray else MaterialTheme.colorScheme.outline
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

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AddCourseItem(
                            modifier = Modifier.weight(1f),
                            label = "起始节 *",
                            value = courseTimeStart,
                            leadingIcon = Icons.Rounded.Notifications,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) courseTimeStart = it },
                            keyboardType = KeyboardType.Number
                        )
                        AddCourseItem(
                            modifier = Modifier.weight(1f),
                            label = "结束节 *",
                            value = courseTimeEnd,
                            leadingIcon = Icons.Rounded.Notifications,
                            onValueChange = { if (it.matches(Regex("^\\d*$"))) courseTimeEnd = it },
                            keyboardType = KeyboardType.Number,
                        )
                    }
                    Text(
                        text = "提示：如课程为1-4节，两框分别填1和4；如只有一节课，两框填相同的数字",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            SectionTitle(icon = Icons.Rounded.LocationOn, title = "地点信息")
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AddCourseItem(
                        label = "课程校区 *",
                        value = courseCampus,
                        leadingIcon = Icons.Rounded.Home,
                        onValueChange = { courseCampus = it }
                    )
                    AddCourseItem(
                        label = "课程地点 *",
                        value = courseLocation,
                        leadingIcon = Icons.Rounded.LocationOn,
                        onValueChange = { courseLocation = it }
                    )
                }
            }

            SectionTitle(icon = Icons.AutoMirrored.Rounded.List, title = "其他详细信息 (选填)")
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AddCourseItem(
                            modifier = Modifier.weight(1f), label = "学分", value = courseCredit,
                            leadingIcon = Icons.Rounded.Star, onValueChange = { courseCredit = it },
                            keyboardType = KeyboardType.Number
                        )
                        AddCourseItem(
                            modifier = Modifier.weight(1f), label = "总学时", value = courseTotalStudyHours,
                            onValueChange = { courseTotalStudyHours = it }, keyboardType = KeyboardType.Number
                        )
                    }
                    AddCourseItem(label = "教学班", value = courseTeachingClass, onValueChange = { courseTeachingClass = it })
                    AddCourseItem(label = "教学班组成", value = courseTeachingClassComposition, onValueChange = { courseTeachingClassComposition = it })
                    AddCourseItem(label = "考核方式", value = courseAssessmentMethods, onValueChange = { courseAssessmentMethods = it })
                    AddCourseItem(label = "课程学时组成", value = courseHourComposition, onValueChange = { courseHourComposition = it })
                    AddCourseItem(label = "周学时", value = courseWeekStudyHours, onValueChange = { courseWeekStudyHours = it }, keyboardType = KeyboardType.Number)
                    AddCourseItem(
                        label = "选课备注", value = courseSelectionNotes,
                        onValueChange = { courseSelectionNotes = it },
                        imeAction = ImeAction.Done,
                        singleLine = false, minLines = 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SectionTitle(icon: ImageVector, title: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun AddCourseItem(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    placeholder: String? = null,
    supportingText: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = placeholder?.let { { Text(it, color = Color.LightGray) } },
        leadingIcon = leadingIcon?.let {
            {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        supportingText = supportingText?.let {
            { Text(it, style = MaterialTheme.typography.bodySmall) }
        },
        modifier = modifier.fillMaxWidth(),
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