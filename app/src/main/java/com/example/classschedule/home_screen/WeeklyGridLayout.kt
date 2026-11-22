package com.example.classschedule.home_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.classschedule.data.course.CourseSimple
import com.example.classschedule.data.schedule.Schedule
import com.example.classschedule.tools.getClassTime
import com.example.classschedule.tools.showToast
import com.example.classschedule.ui.theme.LocalCourseColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun WeeklyGridLayout(
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
                                } catch (e: Exception) {
                                    emptyList()
                                }
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
                                var c = x
                                while (c != r) {
                                    val n = parent[c]
                                    parent[c] = r
                                    c = n
                                }
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
                                        try {
                                            Color(android.graphics.Color.parseColor(course.color))
                                        } catch (e: Exception) {
                                            null
                                        }
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
private fun WeekDay(
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
