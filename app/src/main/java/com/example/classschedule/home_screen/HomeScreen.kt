package com.example.classschedule.home_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.data.course.CourseSimple
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.home_viewmodel.HomeViewModel
import com.example.classschedule.tools.getClassTime
import com.example.classschedule.tools.showToast
import com.example.classschedule.ui.theme.LocalCourseColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    val courseTimeList by viewModel.allCourseTime.collectAsState()
    val totalCourseNumber by viewModel.totalCourseNumber.collectAsState()

    val allWeeks by viewModel.allWeek.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val hasLoad by viewModel.hasLoad.collectAsState()
    val isGridLayout by viewModel.isGridLayout.collectAsState()
    val isTimerFinished by viewModel.isTimerFinished.collectAsState()
    val monDateStr by viewModel.monDateStr.collectAsState()

    var currentWeek by rememberSaveable { mutableIntStateOf(1) }
    var localDateCurrentWeek by rememberSaveable { mutableIntStateOf(1) }

    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val quarterHeight = screenHeight / 3

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false
    )


    LaunchedEffect(startDate) {
        if (startDate.isNotBlank() && !hasLoad) {
            currentWeek = viewModel.calculateCurrentWeek(startDateStr = startDate)
            localDateCurrentWeek = currentWeek
            viewModel.changeLoad()
        }
    }

    LaunchedEffect(currentWeek) {
        delay(150)
        viewModel.loadSimpleCourse(currentWeekDate = currentWeek)
        viewModel.getMonDateStr(startDate = startDate, weeksPassed = (currentWeek - 1).toLong())
    }


    LaunchedEffect(Unit) {
        if (!isTimerFinished) {
            delay(150)
            if (currentWeek != 0) {
                viewModel.getMonDateStr(
                    startDate = startDate,
                    weeksPassed = (currentWeek - 1).toLong()
                )
            }
            if (courseTimeList.isEmpty()) {
                repeat(totalCourseNumber) { i ->
                    viewModel.insertCourseTime(
                        Schedule(
                            courseNumber = i + 1,
                            startTime = "00:00",
                            endTime = "00:00"
                        )
                    )
                }
            }
            viewModel.changeIsFinished()
        }
    }

    val initialDayIndex = remember { (LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6) }
    val pagerState = rememberPagerState(
        initialPage = initialDayIndex,
        pageCount = { viewModel.week.size.coerceAtLeast(1) }
    )

    if (showBottomSheet){
        WeekSelectionSheet(
            onDismiss = { showBottomSheet = false },
            sheetState = sheetState,
            quarterHeight = quarterHeight,
            allWeekCount = allWeeks.toIntOrNull() ?: 20,
            currentWeek = currentWeek,
            onClick = { weekNum ->
                currentWeek = weekNum
            },
            localDateWeek = localDateCurrentWeek
        )
    }

    if (isTimerFinished) {
        Scaffold(
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showBottomSheet = true }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "第 $currentWeek 周",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (currentWeek == localDateCurrentWeek)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.width(2.dp))

                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "选择周数",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
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

                    if (!isGridLayout && viewModel.week.isNotEmpty()) {
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
                    AnimatedContent(
                        targetState = currentWeek,
                        transitionSpec = {
                            val duration = 400
                            if (targetState > initialState) {
                                (slideInHorizontally(tween(duration)) { width -> width } + fadeIn(
                                    tween(duration)
                                )) togetherWith
                                        (slideOutHorizontally(tween(duration)) { width -> -width } + fadeOut(
                                            tween(duration)
                                        ))
                            } else {
                                (slideInHorizontally(tween(duration)) { width -> -width } + fadeIn(
                                    tween(duration)
                                )) togetherWith
                                        (slideOutHorizontally(tween(duration)) { width -> width } + fadeOut(
                                            tween(duration)
                                        ))
                            }
                        },
                        label = "week_switch_animation",
                        modifier = Modifier.graphicsLayer { clip = true }
                    ) { targetWeek ->
                        WeeklyGridLayout(
                            courseList = courseList,
                            weekDays = viewModel.week,
                            navigateToCourseDetails = { id, weekDay ->
                                navigateToCourseDetails(
                                    id,
                                    targetWeek.toString(),
                                    weekDay,
                                    startDate
                                )
                            },
                            totalCourseNUmber = totalCourseNumber,
                            courseTimeList = courseTimeList,
                            onNextWeek = {
                                val maxWeek = allWeeks.toIntOrNull() ?: 20
                                if (currentWeek < maxWeek) {
                                    currentWeek++
                                }
                            },
                            onPrevWeek = {
                                if (currentWeek > 1) {
                                    currentWeek--
                                }
                            },
                            monDateStr = monDateStr
                        )
                    }
                } else {
                    if (viewModel.week.isNotEmpty()) {
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
                                },
                                courseTimeList = courseTimeList
                            )
                        }
                    }
                }
            }
        }
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun DailyCourseList(
    courseList: List<CourseSimple>,
    navigateToCourseDetails: (Int) -> Unit,
    courseTimeList: List<Schedule>
) {
    if (courseList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
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
                    courseTime = getClassTime(item.courseTime, allCourseTime = courseTimeList),
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
                        Icons.Rounded.DateRange, "时间",
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
                        Icons.Rounded.LocationOn, "地点",
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
    navigateToCourseDetails: (Int, String) -> Unit,
    totalCourseNUmber: Int = 20,
    courseTimeList: List<Schedule>,
    onNextWeek: () -> Unit,
    onPrevWeek: () -> Unit,
    monDateStr: String
) {
    val timeColumnWidth = 36.dp
    val sectionHeight = 65.dp
    var offsetX by remember { mutableFloatStateOf(0f) }

    val courseColors = LocalCourseColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX > 150f) {
                            onPrevWeek()
                        } else if (offsetX < -150f) {
                            onNextWeek()
                        }
                        offsetX = 0f
                    },
                    onDragCancel = {
                        offsetX = 0f
                    },
                    onHorizontalDrag = { change, dragAmount ->
                        change.consume()
                        offsetX += dragAmount
                    },
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Spacer(modifier = Modifier.width(timeColumnWidth))
            weekDays.forEach { day ->
                val shortName = day.take(3)
                WeekDay(
                    title = shortName,
                    modifier = Modifier.weight(1f),
                    monDateStr = monDateStr
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Column(modifier = Modifier.width(timeColumnWidth)) {
                for (i in 1..totalCourseNUmber) {
                    val timeStr = remember(i, courseTimeList) {
                        getClassTime("$i-${i}节", courseTimeList).replace("-", "\n")
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(sectionHeight),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = i.toString(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = timeStr,
                                fontSize = 8.sp,
                                lineHeight = 9.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(sectionHeight * totalCourseNUmber)
            ) {
                Column {
                    for (i in 1..totalCourseNUmber) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.5f
                            )
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
                                val timeParts = remember(course.courseTime) {
                                    try {
                                        course.courseTime.substringAfter('(', "")
                                            .substringBefore('节', "").split("-")
                                    } catch (e: Exception) {
                                        emptyList()
                                    }
                                }
                                val startSection = timeParts.getOrNull(0)?.toIntOrNull() ?: 1
                                val endSection =
                                    timeParts.getOrNull(1)?.toIntOrNull() ?: startSection
                                val span = (endSection - startSection + 1).coerceAtLeast(1)

                                val colorIndex =
                                    kotlin.math.abs(course.courseName.hashCode()) % courseColors.colors.size

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = sectionHeight * (startSection - 1))
                                        .height(sectionHeight * span)
                                        .padding(1.5.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(courseColors.colors[colorIndex])
                                        .clickable {
                                            navigateToCourseDetails(
                                                course.id,
                                                currentDay
                                            )
                                        }
                                        .padding(4.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = course.courseName,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = courseColors.textColor.copy(alpha = 0.85f),
                                            maxLines = if (span == 1) 2 else 4,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = course.courseLocation,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = courseColors.textColor.copy(alpha = 0.6f),
                                            maxLines = if (span == 1) 1 else 3,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
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
fun WeekDay(
    title: String,
    modifier: Modifier,
    monDateStr: String,
) {
    val parsedDate = LocalDate.parse(monDateStr)
    val formatter = DateTimeFormatter.ofPattern("M/dd")

    val formattedDate =
        when (title) {
            "Mon" -> parsedDate.format(formatter)
            "Tue" -> {
                parsedDate.plusDays(1).format(formatter)
            }

            "Wed" -> {
                parsedDate.plusDays(2).format(formatter)
            }

            "Thu" -> {
                parsedDate.plusDays(3).format(formatter)
            }

            "Fri" -> {
                parsedDate.plusDays(4).format(formatter)
            }

            "Sat" -> {
                parsedDate.plusDays(5).format(formatter)
            }

            "Sun" -> {
                parsedDate.plusDays(6).format(formatter)
            }

            else -> {
                "时间获取失败".showToast()
                "failure"
            }

        }.toString()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekSelectionSheet(
    onDismiss: () -> Unit,
    sheetState: SheetState,
    quarterHeight: Dp,
    allWeekCount: Int,
    currentWeek: Int,
    localDateWeek: Int,
    onClick: (Int) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = quarterHeight)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "选择周数",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(allWeekCount) { index ->
                    val weekNum = index + 1
                    val isSelected = weekNum == currentWeek
                    val isRealThisWeek = weekNum == localDateWeek

                    val containerColor = when {
                        isSelected -> MaterialTheme.colorScheme.primary
                        isRealThisWeek -> MaterialTheme.colorScheme.primaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    }
                    val contentColor = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        isRealThisWeek -> MaterialTheme.colorScheme.onPrimaryContainer
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(containerColor)
                            .clickable {
                                onClick(weekNum)
                                onDismiss()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$weekNum",
                                fontSize = 18.sp,
                                color = contentColor,
                                fontWeight = if (isSelected || isRealThisWeek) FontWeight.Bold else FontWeight.Medium
                            )

                            if (isRealThisWeek) {
                                Text(
                                    text = "本周",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = contentColor.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}