package com.example.classschedule.home_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import com.example.classschedule.ui.ColorPickerBottomSheet
import com.example.classschedule.ui.GridLayoutGuideSnackbar
import com.example.classschedule.ui.HomeEmptyGuide
import com.example.classschedule.ui.theme.LocalCourseColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

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
        delay(150.milliseconds)
        viewModel.loadSimpleCourse(currentWeekDate = currentWeek)
        viewModel.getMonDateStr(startDate = startDate, weeksPassed = (currentWeek - 1).toLong())
    }


    LaunchedEffect(Unit) {
        if (!isTimerFinished) {
            delay(150.milliseconds)
            if (currentWeek != 0) {
                viewModel.getMonDateStr(
                    startDate = startDate,
                    weeksPassed = (currentWeek - 1).toLong()
                )
            }
            viewModel.changeIsFinished()
        }
    }

    val initialDayIndex = remember { (LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6) }
    val pagerState = rememberPagerState(
        initialPage = initialDayIndex,
        pageCount = { viewModel.week.size.coerceAtLeast(1) }
    )

    var showColorPicker by remember { mutableStateOf(false) }
    var colorPickerTargetId by remember { mutableStateOf(0) }
    var colorPickerInitialColor by remember { mutableStateOf<String?>(null) }


    val guideHomeShown by viewModel.guideHomeShown.collectAsState()
    val guideGridShown by viewModel.guideGridShown.collectAsState()

    val showHomeGuide = remember(courseList, guideHomeShown) {
        courseList.isEmpty() && !guideHomeShown
    }

    var showGridGuide by remember { mutableStateOf(false) }
    LaunchedEffect(isGridLayout, guideGridShown) {
        if (isGridLayout && !guideGridShown) {
            delay(600.milliseconds)
            showGridGuide = true
        }
    }

    if (showColorPicker) {
        ColorPickerBottomSheet(
            initialColor = colorPickerInitialColor,
            onDismiss = { showColorPicker = false },
            onColorSelected = { color ->
                viewModel.updateCourseColor(colorPickerTargetId, color)
                showColorPicker = false
            }
        )
    }

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
        Box(modifier = Modifier.fillMaxSize()) {
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
                                if (currentWeek < maxWeek) currentWeek++
                            },
                            onPrevWeek = {
                                if (currentWeek > 1) currentWeek--
                            },
                            monDateStr = monDateStr,
                            onLongClickCourse = { id, currentColor ->
                                colorPickerTargetId = id
                                colorPickerInitialColor = currentColor
                                showColorPicker = true
                            }
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
                                courseTimeList = courseTimeList,
                                onColorChange = { id, currentColor ->
                                    colorPickerTargetId = id
                                    colorPickerInitialColor = currentColor
                                    showColorPicker = true
                                }
                            )
                        }
                    }
                }
            }
        }


        HomeEmptyGuide(
            visible = showHomeGuide,
            onGoToSetting = navigateToSetting,
            onDismiss = { viewModel.markGuideHomeShown() }
        )


        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            GridLayoutGuideSnackbar(
                visible = showGridGuide,
                onDismiss = {
                    showGridGuide = false
                    viewModel.markGuideGridShown()
                }
            )
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
    courseTimeList: List<Schedule>,
    onColorChange: (id: Int, color: String?) -> Unit
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
                    courseColor = item.color,
                    onClick = { navigateToCourseDetails(item.id) },
                    onLongClick = { onColorChange(item.id, item.color) }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ScheduleCard(
    courseName: String,
    courseTime: String,
    courseLocation: String,
    courseColor: String?,
    onClick: () -> Unit,
    onLongClick: () -> Unit
) {
    val accentColor = remember(courseColor) {
        if (courseColor != null) {
            try { Color(android.graphics.Color.parseColor(courseColor)) }
            catch (e: Exception) { null }
        } else null
    }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor ?: MaterialTheme.colorScheme.primary)
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WeeklyGridLayout(
    courseList: List<CourseSimple>,
    weekDays: List<String>,
    navigateToCourseDetails: (Int, String) -> Unit,
    totalCourseNUmber: Int = 20,
    courseTimeList: List<Schedule>,
    onNextWeek: () -> Unit,
    onPrevWeek: () -> Unit,
    monDateStr: String,
    onLongClickCourse: (id: Int, currentColor: String?) -> Unit
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
                        BoxWithConstraints(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            val columnWidth = maxWidth

                            data class CourseSlot(
                                val course: CourseSimple,
                                val start: Int,
                                val end: Int,
                                val span: Int
                            )

                            val slots = dayCourses.map { course ->
                                val parts = try {
                                    course.courseTime.substringAfter('(', "")
                                        .substringBefore('节', "").split("-")
                                } catch (e: Exception) { emptyList() }
                                val s = parts.getOrNull(0)?.toIntOrNull() ?: 1
                                val e = parts.getOrNull(1)?.toIntOrNull() ?: s
                                CourseSlot(course, s, e, (e - s + 1).coerceAtLeast(1))
                            }

                            val trackIndex = IntArray(slots.size) { 0 }
                            val trackEndAt = mutableListOf<Int>()
                            val sortedIndices = slots.indices.sortedBy { slots[it].start }
                            sortedIndices.forEach { i ->
                                val slot = slots[i]
                                val track = trackEndAt.indexOfFirst { it < slot.start }
                                if (track == -1) {
                                    trackIndex[i] = trackEndAt.size
                                    trackEndAt.add(slot.end)
                                } else {
                                    trackIndex[i] = track
                                    trackEndAt[track] = slot.end
                                }
                            }

                            val parent = IntArray(slots.size) { it }
                            fun find(x: Int): Int {
                                var r = x
                                while (parent[r] != r) r = parent[r]
                                var c = x; while (c != r) { val n = parent[c]; parent[c] = r; c = n }
                                return r
                            }
                            for (a in slots.indices) for (b in a + 1 until slots.size) {
                                if (slots[a].start <= slots[b].end && slots[b].start <= slots[a].end)
                                    parent[find(a)] = find(b)
                            }
                            val groupTracks = mutableMapOf<Int, Int>()
                            slots.indices.forEach { i ->
                                val root = find(i)
                                groupTracks[root] = maxOf(groupTracks.getOrDefault(root, 0), trackIndex[i] + 1)
                            }
                            val slotColTotal = IntArray(slots.size) { i -> groupTracks[find(i)] ?: 1 }

                            slots.forEachIndexed { i, slot ->
                                val course = slot.course
                                val colIdx = trackIndex[i]
                                val colTotal = slotColTotal[i]
                                val slotWidth = columnWidth / colTotal

                                val colorIndex =
                                    kotlin.math.abs(course.courseName.hashCode()) % courseColors.colors.size
                                val cardBgColor = remember(course.color) {
                                    if (course.color != null) {
                                        try { Color(android.graphics.Color.parseColor(course.color)) }
                                        catch (e: Exception) { null }
                                    } else null
                                } ?: courseColors.colors[colorIndex]

                                Box(
                                    modifier = Modifier
                                        .width(slotWidth)
                                        .offset(
                                            x = slotWidth * colIdx,
                                            y = sectionHeight * (slot.start - 1)
                                        )
                                        .height(sectionHeight * slot.span)
                                        .padding(1.5.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(cardBgColor)
                                        .combinedClickable(
                                            onClick = { navigateToCourseDetails(course.id, currentDay) },
                                            onLongClick = { onLongClickCourse(course.id, course.color) }
                                        )
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
                                            maxLines = if (slot.span == 1) 2 else 4,
                                            overflow = TextOverflow.Ellipsis,
                                            textAlign = TextAlign.Center
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = course.courseLocation,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = courseColors.textColor.copy(alpha = 0.6f),
                                            maxLines = if (slot.span == 1) 1 else 3,
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