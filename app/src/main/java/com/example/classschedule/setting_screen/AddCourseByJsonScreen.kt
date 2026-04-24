package com.example.classschedule.setting_screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.ArrowBackIosNew
import androidx.compose.material.icons.outlined.FolderOpen
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.course.Course
import com.example.classschedule.setting_viewmodel.AddCourseByJsonViewModel
import com.example.classschedule.tools.showToast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseByJson(
    viewModel: AddCourseByJsonViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateUp: () -> Unit,
    navigateToHome: () -> Unit
) {
    val courseList by viewModel.courseList.collectAsState()
    val context = LocalContext.current

    val importLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.importJsonFromUri(context = context, uri = uri)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "导入课程") },
                navigationIcon = {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Outlined.ArrowBackIosNew,
                            contentDescription = "返回"
                        )
                    }
                },
                actions = {
                    if (!courseList.isNullOrEmpty()) {
                        IconButton(onClick = { importLauncher.launch(arrayOf("application/json")) }) {
                            Icon(
                                imageVector = Icons.Outlined.FolderOpen,
                                contentDescription = "重新导入"
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            if (!courseList.isNullOrEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val coursesToInsert = courseList?.map { it.copy(id = 0) } ?: emptyList()
                        viewModel.insertCourseList(courseList = coursesToInsert)
                        "添加成功".showToast()
                        navigateToHome()
                    },
                    icon = { Icon(Icons.Default.Check, contentDescription = "确认添加") },
                    text = { Text("确认添加 ${courseList?.size ?: 0} 门课") }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (courseList.isNullOrEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Outlined.FolderOpen,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "暂无课程数据", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { importLauncher.launch(arrayOf("application/json")) }) {
                        Text("选择 JSON 文件导入")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = courseList ?: emptyList(),
                        key = { course -> course.id }
                    ) { course ->
                        CourseItemCard(course = course)
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItemCard(course: Course) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(MaterialTheme.colorScheme.primary)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Text(
                    text = course.courseName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                CourseDetailRow(
                    icon = Icons.Rounded.Person,
                    text = "教师: ${course.courseTeacher}"
                )

                CourseDetailRow(
                    icon = Icons.Rounded.AccessTime,
                    text = "${course.weekDay} ${course.courseTime}"
                )

                CourseDetailRow(
                    icon = Icons.Rounded.LocationOn,
                    text = course.courseLocation
                )
            }
        }
    }
}

@Composable
private fun CourseDetailRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}