package com.example.classschedule.home_screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.classschedule.home_viewmodel.HomeViewModel
import com.example.classschedule.ui.ColorPickerBottomSheet
import com.example.classschedule.ui.GridLayoutGuideSnackbar
import com.example.classschedule.ui.HomeEmptyGuide
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navigateToAddCourse: () -> Unit,
    navigateToCourseDetails: (Int, String, String, String) -> Unit,
    navigateToSetting: () -> Unit,
    sharedTransitionScope: SharedTransitionScope? = null,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
) {
    val scope = rememberCoroutineScope()
    val courseList by viewModel.courseList.collectAsStateWithLifecycle()
    val courseTimeList by viewModel.allCourseTime.collectAsStateWithLifecycle()
    val totalCourseNumber by viewModel.totalCourseNumber.collectAsStateWithLifecycle()

    val allWeeks by viewModel.allWeek.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val hasLoad by viewModel.hasLoad.collectAsStateWithLifecycle()
    val isGridLayout by viewModel.isGridLayout.collectAsStateWithLifecycle()
    val isTimerFinished by viewModel.isTimerFinished.collectAsStateWithLifecycle()
    val monDateStr by viewModel.monDateStr.collectAsStateWithLifecycle()

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

    val guideHomeShown by viewModel.guideHomeShown.collectAsStateWithLifecycle()
    val guideGridShown by viewModel.guideGridShown.collectAsStateWithLifecycle()

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

    if (showBottomSheet) {
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
                                    },
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope
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
