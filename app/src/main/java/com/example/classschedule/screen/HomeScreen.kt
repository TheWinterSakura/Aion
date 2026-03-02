package com.example.classschedule.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.tools.showToast
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navigateToAddCourse: () -> Unit,
    navigateToCourseDetails: () -> Unit
) {

    val pagerState = rememberPagerState(pageCount = { viewModel.week.size })
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val allWeeks by remember { mutableIntStateOf(20) }
    var expanded by remember { mutableStateOf(false) }
    var currentWeek by remember { mutableIntStateOf(1) }

    Scaffold(
        modifier = Modifier.padding(),
        topBar = {

            Column {
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(end = 28.dp)
                        .align(Alignment.End)
                        .clickable(
                            onClick = { expanded = true }
                        )
                ) {
                    Text(
                        text = "第${currentWeek}周",
                        fontSize = 28.sp
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        },
                        modifier = Modifier.heightIn(max = 300.dp)
                    ) {
                        repeat(allWeeks) { i ->
                            DropdownMenuItem(
                                text = {
                                    Text("第${i + 1}周")
                                },
                                onClick = {
                                    currentWeek = i + 1
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                ScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                ) {
                    viewModel.week.forEachIndexed { index, title ->
                        Tab(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = { Text(text = title) }
                        )
                    }
                }

            }

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navigateToAddCourse() },
                containerColor = Color.LightGray,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "添加"
                )
            }
        },
    ) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) { page ->
            when (page) {
                0 -> {
                    Monday(
                        navigateToCourseDetails = {
                            navigateToCourseDetails()
                        }
                    )
                }

                1 -> {
                    Tuesday()
                }

                2 -> {
                    Wednesday()
                }

                3 -> {
                    Thursday()
                }

                4 -> {
                    Friday()
                }

                5 -> {
                    Saturday()
                }

                6 -> {
                    Sunday()
                }

                else -> {
                    "下标越界".showToast(context)
                }
            }
        }
    }
}


@Composable
fun Monday(
    navigateToCourseDetails: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 24.dp)
    ) {
        ScheduleCard(
            courseName = "Math",
            courseTime = "10:00 - 11:00",
            courseLocation = "石家庄",
            navigateToCourseDetails = {
                navigateToCourseDetails()
            }
        )
    }
}

@Composable
fun Tuesday() {

}

@Composable
fun Wednesday() {

}

@Composable
fun Thursday() {

}

@Composable
fun Friday() {

}

@Composable
fun Saturday() {

}

@Composable
fun Sunday() {

}

@Composable
fun ScheduleCard(
    courseName: String,
    courseTime: String,
    courseLocation: String,
    navigateToCourseDetails: () -> Unit,
) {
    Card(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(
                onClick = {
                    navigateToCourseDetails()
                }
            ),
        colors = CardDefaults.cardColors(Color.Green)
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 8.dp)
        ) {
            Text(
                text = "课程名称：$courseName",
                modifier = Modifier.padding(),
                color = Color.White
            )
            Text(
                text = "课程时间：$courseTime",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.White
            )
            Text(
                text = "课程地点：$courseLocation",
                modifier = Modifier.padding(top = 8.dp),
                color = Color.White
            )
        }
    }
}


