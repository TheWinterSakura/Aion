package com.example.classschedule.home_screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.CourseSimple
import com.example.classschedule.tools.getClassTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToAddCourse: () -> Unit,
    navigateToCourseDetails: (Int, String, String, String) -> Unit,
    navigateToSetting: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val courseList by viewModel.courseList.collectAsState()

    val allWeeks by viewModel.allWeek.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val startDate by viewModel.startDate.collectAsState()

    var currentWeek by rememberSaveable { mutableIntStateOf(1) }
    val hasLoad by viewModel.hasLoad.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()
    val isTimerFinished by viewModel.isTimerFinished.collectAsState()

    if (startDate.isNotBlank() && !hasLoad) {
        LaunchedEffect(Unit) {
            currentWeek = viewModel.calculateCurrentWeek(startDateStr = startDate)
            viewModel.changeLoad()
        }
    }

    val initialDayIndex = remember { LocalDate.now().dayOfWeek.value - 1 }

    val pagerState = rememberPagerState(
        initialPage = initialDayIndex,
        pageCount = { viewModel.week.size }
    )

    LaunchedEffect(currentWeek) {
        viewModel.loadSimpleCourse(currentWeekDate = currentWeek)
    }

    if (!isTimerFinished){
        LaunchedEffect(Unit) {
            delay(500)
            viewModel.changeIsFinished()
        }
    }

    if (isTimerFinished) {
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
                                    Text(
                                        text = "第 $currentWeek 周",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Icon(
                                        Icons.Default.ArrowDropDown,
                                        contentDescription = "选择周数"
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.heightIn(max = 400.dp)
                                ) {
                                    val weeksCount = allWeeks.toIntOrNull() ?: 20
                                    repeat(weeksCount) { i ->
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
                            IconButton(onClick = navigateToSetting) {
                                Icon(
                                    imageVector = Icons.Rounded.Settings,
                                    contentDescription = "设置"
                                )
                            }
                        }
                    )

                    if (!isGridLayout) {
                        ScrollableTabRow(
                            modifier = Modifier.clipToBounds(),
                            selectedTabIndex = pagerState.currentPage,
                            edgePadding = 16.dp,
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.primary,
                            divider = {}
                        ) {
                            viewModel.week.forEachIndexed { index, title ->
                                val isSelected = pagerState.currentPage == index
                                val isToday = index == initialDayIndex
                                Tab(
                                    selected = isSelected,
                                    onClick = {
                                        scope.launch { pagerState.animateScrollToPage(index) }
                                    },
                                    text = {
                                        Text(
                                            text = title,
                                            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isToday && !isSelected) MaterialTheme.colorScheme.primary else Color.Unspecified
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = navigateToAddCourse,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    shape = CircleShape
                ) {
                    Icon(Icons.Default.Add, contentDescription = "添加课程")
                }
            },
        ) { innerPadding ->
            AnimatedContent(
                targetState = isGridLayout,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                transitionSpec = {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                },
                label = "layout_switch"
            ) { targetIsGrid ->
                if (targetIsGrid) {
                    WeeklyGridLayout(
                        courseList = courseList,
                        weekDays = viewModel.week,
                        navigateToCourseDetails = { id, weekDay ->
                            navigateToCourseDetails(id, currentWeek.toString(), weekDay, startDate)
                        }
                    )
                } else {
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        val currentDayString = viewModel.week[page]
                        val dailyCourses = remember(courseList, currentDayString) {
                            courseList.filter { it.weekDay == currentDayString }
                        }
                        DailyCourseList(
                            courseList = dailyCourses,
                            navigateToCourseDetails = { id ->
                                navigateToCourseDetails(
                                    id,
                                    currentWeek.toString(),
                                    currentDayString,
                                    startDate
                                )
                            }
                        )
                    }
                }
            }
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DailyCourseList(courseList: List<CourseSimple>, navigateToCourseDetails: (Int) -> Unit) {
    if (courseList.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "今天没有课哦，好好休息吧~",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(items = courseList, key = { item -> item.id }) { item ->
                ScheduleCard(
                    courseName = item.courseName,
                    courseTime = getClassTime(item.courseTime),
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
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick, modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
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
                    courseName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.DateRange,
                        "时间",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        courseTime,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.LocationOn,
                        "地点",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        courseLocation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyGridLayout(
    courseList: List<CourseSimple>,
    weekDays: List<String>,
    navigateToCourseDetails: (Int, String) -> Unit
) {
    val timeColumnWidth = 36.dp
    val sectionHeight = 65.dp
    val maxSections = 13

    val cardColors = listOf(
        Color(0xFFE3F2FD), Color(0xFFF3E5F5), Color(0xFFE8F5E9),
        Color(0xFFFFF3E0), Color(0xFFFFEBEE), Color(0xFFE0F7FA)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(timeColumnWidth))
            weekDays.forEach { day ->
                when (day) {
                    "Monday" -> WeekDay(title = "Mon", modifier = Modifier.weight(1f))
                    "Tuesday" -> WeekDay(title = "Tue", modifier = Modifier.weight(1f))
                    "Wednesday" -> WeekDay(title = "Wed", modifier = Modifier.weight(1f))
                    "Thursday" -> WeekDay(title = "Thu", modifier = Modifier.weight(1f))
                    "Friday" -> WeekDay(title = "Fri", modifier = Modifier.weight(1f))
                    "Saturday" -> WeekDay(title = "Sat", modifier = Modifier.weight(1f))
                    "Sunday" -> WeekDay(title = "Sun", modifier = Modifier.weight(1f))
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier.width(timeColumnWidth)
            ) {
                for (i in 1..maxSections) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sectionHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = i.toString(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(sectionHeight * maxSections)
            ) {
                Column {
                    for (i in 1..maxSections) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(sectionHeight - 1.dp))
                    }
                }

                Row(modifier = Modifier.fillMaxSize()) {
                    weekDays.forEach { currentDay ->
                        val dayCourses = courseList.filter { it.weekDay == currentDay }
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            dayCourses.forEach { course ->
                                val timeParts = course.courseTime
                                    .substringAfter('(')
                                    .substringBefore('节')
                                    .split("-")
                                val startSection = timeParts.getOrNull(0)?.toIntOrNull() ?: 1
                                val endSection =
                                    timeParts.getOrNull(1)?.toIntOrNull() ?: startSection
                                val span = (endSection - startSection + 1).coerceAtLeast(1)

                                val colorIndex =
                                    kotlin.math.abs(course.courseName.hashCode()) % cardColors.size

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = sectionHeight * (startSection - 1))
                                        .height(sectionHeight * span)
                                        .padding(1.5.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(cardColors[colorIndex])
                                        .clickable {
                                            navigateToCourseDetails(
                                                course.id,
                                                currentDay
                                            )
                                        }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column {
                                        Text(
                                            text = course.courseName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.Black.copy(alpha = 0.8f),
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = course.courseLocation,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = Color.Black.copy(alpha = 0.6f),
                                            maxLines = 5,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeekDay(title: String, modifier: Modifier) {
    Text(
        text = title,
        modifier = modifier,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}