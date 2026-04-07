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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.classschedule.home_viewmodel.AddCourseViewModel

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

    var showPeriodDialog by remember { mutableStateOf(false) }
    var startPeriod by remember { mutableIntStateOf(1) }
    var endPeriod by remember { mutableIntStateOf(2) }

    val maxPeriodsPerDay by viewModel.maxPeriodsPerDay.collectAsState()

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

    val week = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    val isFormValid = courseName.isNotBlank() &&
            startWeekDate.isNotBlank() &&
            endWeekDate.isNotBlank() &&
            weekDay.isNotBlank() &&
            courseCampus.isNotBlank() &&
            courseLocation.isNotBlank()

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
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                                Icon(
                                    Icons.Rounded.DateRange,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
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

                    // 课程节数选择 (点击弹窗)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
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
                            label = { Text("课程节数 *") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Notifications,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                    modifier = Modifier.size(20.dp)
                                )
                            },
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
            }

            SectionTitle(icon = Icons.Rounded.LocationOn, title = "地点信息")
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
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
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        AddCourseItem(
                            modifier = Modifier.weight(1f), label = "学分", value = courseCredit,
                            leadingIcon = Icons.Rounded.Star, onValueChange = { courseCredit = it },
                            keyboardType = KeyboardType.Number
                        )
                        AddCourseItem(
                            modifier = Modifier.weight(1f),
                            label = "总学时",
                            value = courseTotalStudyHours,
                            onValueChange = { courseTotalStudyHours = it },
                            keyboardType = KeyboardType.Number
                        )
                    }
                    AddCourseItem(
                        label = "教学班",
                        value = courseTeachingClass,
                        onValueChange = { courseTeachingClass = it })
                    AddCourseItem(
                        label = "教学班组成",
                        value = courseTeachingClassComposition,
                        onValueChange = { courseTeachingClassComposition = it })
                    AddCourseItem(
                        label = "考核方式",
                        value = courseAssessmentMethods,
                        onValueChange = { courseAssessmentMethods = it })
                    AddCourseItem(
                        label = "课程学时组成",
                        value = courseHourComposition,
                        onValueChange = { courseHourComposition = it })
                    AddCourseItem(
                        label = "周学时",
                        value = courseWeekStudyHours,
                        onValueChange = { courseWeekStudyHours = it },
                        keyboardType = KeyboardType.Number
                    )
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