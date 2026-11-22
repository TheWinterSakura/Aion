package com.example.classschedule.home_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
internal fun PeriodSelectionDialog(
    maxPeriods: Int,
    initialStart: Int,
    initialEnd: Int,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {
    var tempStart by remember { mutableIntStateOf(initialStart) }
    var tempEnd by remember { mutableIntStateOf(initialEnd) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "选择课程节数", fontWeight = FontWeight.Bold) },
        text = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "起始节",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items((1..maxPeriods).toList()) { period ->
                            val isSelected = tempStart == period
                            Text(
                                text = "第 $period 节",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        tempStart = period
                                        if (tempEnd < tempStart) tempEnd = tempStart
                                    }
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Text(
                    "至",
                    modifier = Modifier.padding(horizontal = 8.dp),
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "结束节",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn(
                        modifier = Modifier.weight(1f)
                    ) {
                        items((1..maxPeriods).toList()) { period ->
                            val isSelected = tempEnd == period
                            Text(
                                text = "第 $period 节",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                                        shape = MaterialTheme.shapes.small
                                    )
                                    .clickable {
                                        if (period >= tempStart) {
                                            tempEnd = period
                                        }
                                    }
                                    .padding(vertical = 12.dp),
                                textAlign = TextAlign.Center,
                                color = if (period < tempStart) Color.Gray.copy(alpha = 0.5f)
                                else if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                                else MaterialTheme.colorScheme.onSurface,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(tempStart, tempEnd) }) {
                Text("确定")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
