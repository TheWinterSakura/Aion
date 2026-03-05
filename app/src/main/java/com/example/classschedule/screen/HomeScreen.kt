package com.example.classschedule.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.CourseSimple
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToAddCourse: () -> Unit,
    navigateToCourseDetails: (Int) -> Unit,
    navigateToSetting: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val courseList by viewModel.courseList.collectAsState()

    val allWeeks by viewModel.allWeek.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val startDate by viewModel.startDate.collectAsState()

    var currentWeek by remember { mutableIntStateOf(1) }

    LaunchedEffect(startDate) {
        if (startDate != ""){
            currentWeek = viewModel.calculateCurrentWeek(startDateStr = startDate)
        }
    }

    val initialDayIndex = remember { LocalDate.now().dayOfWeek.value - 1 }

    val pagerState = rememberPagerState(
        initialPage = initialDayIndex,
        pageCount = { viewModel.week.size }
    )

    LaunchedEffect(currentWeek, pagerState.currentPage) {
        val currentDayString = viewModel.week[pagerState.currentPage]
        viewModel.loadSimpleCourse(currentWeekDate = currentWeek, weekDay = currentDayString)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clickable { expanded = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(text = "第 $currentWeek 周", fontSize = 22.sp)
                                Icon(Icons.Default.ArrowDropDown, contentDescription = "选择周数")
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier.heightIn(max = 400.dp)
                            ) {
                                repeat(allWeeks.toInt()) { i ->
                                    DropdownMenuItem(
                                        text = { Text("第 ${i + 1} 周") },
                                        onClick = {
                                            currentWeek = i + 1
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                navigateToSetting()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "setting"
                            )
                        }
                    }
                )

                ScrollableTabRow(
                    modifier = Modifier.clipToBounds(),
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    viewModel.week.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch { pagerState.animateScrollToPage(index) }
                            },
                            text = {
                                Text(
                                    text = title,
                                    color = if (index == initialDayIndex) MaterialTheme.colorScheme.primary else Color.Unspecified
                                )
                            }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddCourse() },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "添加课程")
            }
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { page ->
            DailyCourseList(
                courseList = courseList,
                navigateToCourseDetails = navigateToCourseDetails
            )
        }
    }
}

@Composable
fun DailyCourseList(
    courseList: List<CourseSimple>,
    navigateToCourseDetails: (Int) -> Unit
) {
    if (courseList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("今天没有课哦，好好休息吧~", color = Color.Gray)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items = courseList, key = { item -> item.id }) { item ->
                ScheduleCard(
                    courseName = item.courseName,
                    courseTime = item.courseTime,
                    courseLocation = item.courseLocation,
                    onClick = { navigateToCourseDetails(item.id) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleCard(
    courseName: String,
    courseTime: String,
    courseLocation: String,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🕒", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = courseTime, style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "📍", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = courseLocation, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}