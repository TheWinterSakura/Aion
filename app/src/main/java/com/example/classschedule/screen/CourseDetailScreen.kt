package com.example.classschedule.screen

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.tools.getClassTime
import com.example.classschedule.tools.showToast

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetail(
    courseId: Int,
    weekDate: String,
    dayDate: String,
    startDate: String,
    navigationUp: () -> Unit,
    navigateToEdit: (Int) -> Unit,
    viewModel: CourseDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(courseId) {
        viewModel.loadCourse(id = courseId)
    }

    val course by viewModel._course.collectAsState()
    val context = LocalContext.current
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    fun executeAddAlert() {
        if (course != null) {
            viewModel.addAlert(
                course = course!!,
                weekDate = weekDate,
                dayDate = dayDate,
                startDate = startDate,
                context = context
            )
        }
    }

    val notificationLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            executeAddAlert()
        } else {
            "需要通知权限才行".showToast()
        }
    }

    val exactAlarmLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val notifGranted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED

                    if (notifGranted) {
                        executeAddAlert()
                    } else {
                        notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                } else {
                    executeAddAlert()
                }
            } else {
                "需开启精确闹钟权限才能设置提醒".showToast()
            }
        }
    }

    fun checkPermissionsAndAddAlert() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = Uri.parse("package:${context.packageName}")
            }
            exactAlarmLauncher.launch(intent)
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notifGranted = ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!notifGranted) {
                notificationLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                return
            }
        }
        executeAddAlert()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Course Details") },
                navigationIcon = {
                    IconButton(onClick = navigationUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    checkPermissionsAndAddAlert()
                    viewModel.addSchedule(
                        context = context,
                        title = course?.courseName ?: "未查询到课程名称",
                        location = course?.courseLocation?.split(' ')[1] ?: "未查询到课程地点",
                        weekDate = weekDate,
                        dayDate = dayDate,
                        startDate = startDate,
                        courseTime = course?.courseTime?:"课程时间获取失败"
                    )
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加提醒")
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        if (course == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = course?.courseName ?: "Unknown Course",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                )

                CourseInfoCard(title = "Time & Location") {
                    DetailItem(
                        icon = Icons.Default.DateRange,
                        label = "Week",
                        value = "${course?.startWeekDate} - ${course?.endWeekDate}"
                    )
                    DetailItem(
                        icon = Icons.Default.DateRange,
                        label = "Time",
                        value = "${
                            course?.courseTime?.substringAfter('(')?.substringBefore(')') ?: ""
                        } ( ${
                            getClassTime(
                                course?.courseTime ?: ""
                            )
                        } )"
                    )
                    DetailItem(
                        icon = Icons.Default.Place,
                        label = "Campus",
                        value = course?.courseCampus
                    )
                    DetailItem(
                        icon = Icons.Default.LocationOn,
                        label = "Location",
                        value = course?.courseLocation?.split(" ")[1] ?: "无"
                    )
                }

                CourseInfoCard(title = "Class Information") {
                    DetailItem(
                        icon = Icons.Default.Person,
                        label = "Teacher",
                        value = course?.courseTeacher
                    )
                    DetailItem(
                        icon = Icons.Default.Menu,
                        label = "Teaching Class",
                        value = course?.courseTeachingClass
                    )
                    DetailItem(
                        icon = Icons.Default.AccountBox,
                        label = "Class Composition",
                        value = course?.courseTeachingClassComposition
                    )
                }

                CourseInfoCard(title = "Hours & Credits") {
                    DetailItem(
                        icon = Icons.Default.Star,
                        label = "Credits",
                        value = course?.courseCredit
                    )
                    DetailItem(
                        icon = Icons.Default.Info,
                        label = "Hour Composition",
                        value = course?.courseHourComposition
                    )
                    DetailItem(
                        icon = Icons.Default.Refresh,
                        label = "Weekly Study Hours",
                        value = course?.courseWeekStudyHours
                    )
                    DetailItem(
                        icon = Icons.Default.Done,
                        label = "Total Study Hours",
                        value = course?.courseTotalStudyHours
                    )
                }

                if (!course?.courseSelectionNotes.isNullOrEmpty()) {
                    CourseInfoCard(title = "Notes") {
                        Text(
                            text = course?.courseSelectionNotes ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        ),
                        border = BorderStroke(
                            1.dp,
                            MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Delete", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { navigateToEdit(courseId) },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Edit", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete Course") },
            text = { Text("Are you sure you want to delete '${course?.courseName}'? This action cannot be undone.") },
            icon = {
                Icon(
                    Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = MaterialTheme.colorScheme.error
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        viewModel.deleteCourse(course = course!!)
                        "删除成功".showToast(context)
                        navigationUp()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun CourseInfoCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )
            HorizontalDivider(
                modifier = Modifier.padding(bottom = 8.dp, top = 4.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
            content()
        }
    }
}

@Composable
fun DetailItem(icon: ImageVector, label: String, value: String?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (value.isNullOrEmpty()) "N/A" else value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}