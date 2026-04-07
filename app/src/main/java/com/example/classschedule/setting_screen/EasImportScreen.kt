package com.example.classschedule.setting_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.UnfoldMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.classschedule.AppViewModelProvider
import com.example.classschedule.school.University
import com.example.classschedule.setting_viewmodel.EasImportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasImport(
    onNavigateBack: () -> Unit,
    viewModel: EasImportViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateToWeb: (String) -> Unit,
) {
    val context = LocalContext.current
    val universityList by viewModel.universityList.collectAsState()
    val universityUrl by viewModel.universityUrl.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadSchool(context = context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("教务系统导入") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                }
            )
        }
    ) { paddingValues ->
        EasImportContent(
            modifier = Modifier.padding(paddingValues),
            universityUrl = universityUrl,
            universityList = universityList,
            onUniversitySelected = { viewModel.saveUniversity(universityUrl = it.schoolUrl) },
            onNavigateToWeb = onNavigateToWeb
        )
    }
}

@Composable
private fun EasImportContent(
    modifier: Modifier = Modifier,
    universityUrl: String,
    universityList: List<University>,
    onUniversitySelected: (University) -> Unit,
    onNavigateToWeb: (String) -> Unit
) {
    var showUniversityDialog by remember { mutableStateOf(false) }

    if (showUniversityDialog) {
        UniversityDialog(
            universityList = universityList,
            onDismissRequest = { showUniversityDialog = false },
            onClick = { university ->
                onUniversitySelected(university)
                showUniversityDialog = false
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "第一步：选择你的学校",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        val selectedUniversity = remember(universityUrl, universityList) {
            universityList.find { it.schoolUrl == universityUrl }
        }

        SchoolSelector(
            selectedSchoolName = selectedUniversity?.schoolName,
            onClick = { showUniversityDialog = true }
        )

        if (selectedUniversity != null) {
            Text(
                text = "网址: $universityUrl",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, start = 4.dp)
            )
        }

        Spacer(modifier = Modifier.padding(vertical = 12.dp).fillMaxWidth())

        Text(
            text = "第二步：登录并导入",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Button(
            onClick = { onNavigateToWeb(universityUrl) },
            modifier = Modifier.fillMaxWidth(),
            enabled = universityUrl.isNotBlank()
        ) {
            Text(text = "开始导入课程表", style = MaterialTheme.typography.bodyLarge)
        }
    }
}


@Composable
private fun SchoolSelector(
    selectedSchoolName: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = "当前学校",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = selectedSchoolName ?: "请选择学校",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = if (selectedSchoolName != null) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (selectedSchoolName != null) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                imageVector = Icons.Default.UnfoldMore,
                contentDescription = "选择学校",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun UniversityDialog(
    universityList: List<University>,
    onDismissRequest: () -> Unit,
    onClick: (University) -> Unit,
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredList = remember(searchQuery, universityList) {
        if (searchQuery.isBlank()) universityList
        else universityList.filter { it.schoolName.contains(searchQuery, ignoreCase = true) }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth().heightIn(max = 600.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                Text(
                    text = "选择学校",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
                    placeholder = { Text("搜索学校名称...") },
                    leadingIcon = { Icon(Icons.Default.Search, "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, "Clear")
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                if (filteredList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("未找到匹配的学校", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                } else {
                    LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
                        items(items = filteredList, key = { it.schoolUrl }) { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick(item) }
                                    .padding(horizontal = 24.dp, vertical = 16.dp)
                            ) {
                                Text(text = item.schoolName, style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }
        }
    }
}