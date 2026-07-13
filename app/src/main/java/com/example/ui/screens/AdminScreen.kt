package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.SeerahViewModel
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CelestialBlue
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.RadiantGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(
    viewModel: SeerahViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Add, 1: AI Generator, 2: Batch Import, 3: Analytics

    val allDbQuestions by viewModel.allQuestions.collectAsState()
    val customCount = allDbQuestions.count { it.isCustom }
    val history by viewModel.quizHistory.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Panel & MCQ Creator", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepNavy)
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            // Admin Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = DeepNavy,
                contentColor = RadiantGold,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth().testTag("admin_tabs")
            ) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Create MCQ", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("AI Generator", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Batch Import", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
                Tab(selected = selectedTab == 3, onClick = { selectedTab = 3 }, text = { Text("Analytics", fontSize = 12.sp, fontWeight = FontWeight.Bold) })
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (selectedTab) {
                    0 -> ManualAddView(viewModel)
                    1 -> AiGeneratorView(viewModel)
                    2 -> BatchImportView(viewModel, allDbQuestions.size)
                    3 -> AnalyticsView(allDbQuestions.size, customCount, history)
                }
            }
        }
    }
}

// === TAB 1: Manual Add MCQ View ===
@Composable
fun ManualAddView(viewModel: SeerahViewModel) {
    var qText by remember { mutableStateOf("") }
    var optA by remember { mutableStateOf("") }
    var optB by remember { mutableStateOf("") }
    var optC by remember { mutableStateOf("") }
    var optD by remember { mutableStateOf("") }
    var correctOpt by remember { mutableStateOf("") }
    var explanationText by remember { mutableStateOf("") }
    var categoryText by remember { mutableStateOf("Birth") }
    var moduleText by remember { mutableStateOf("Module 1") }
    var difficultyText by remember { mutableStateOf("Medium") }

    var successMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = qText,
            onValueChange = { qText = it },
            label = { Text("Question Text") },
            modifier = Modifier.fillMaxWidth().testTag("admin_q_text"),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = optA,
                onValueChange = { optA = it },
                label = { Text("Option A") },
                modifier = Modifier.weight(1f).testTag("admin_opt_a"),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            OutlinedTextField(
                value = optB,
                onValueChange = { optB = it },
                label = { Text("Option B") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = optC,
                onValueChange = { optC = it },
                label = { Text("Option C") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            OutlinedTextField(
                value = optD,
                onValueChange = { optD = it },
                label = { Text("Option D") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
        }

        OutlinedTextField(
            value = correctOpt,
            onValueChange = { correctOpt = it },
            label = { Text("Correct Answer String") },
            placeholder = { Text("Must match correct option exactly") },
            modifier = Modifier.fillMaxWidth().testTag("admin_correct_opt"),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        OutlinedTextField(
            value = explanationText,
            onValueChange = { explanationText = it },
            label = { Text("Explanation & Source Proof") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = categoryText,
                onValueChange = { categoryText = it },
                label = { Text("Category (e.g. Birth, Badr)") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
            OutlinedTextField(
                value = moduleText,
                onValueChange = { moduleText = it },
                label = { Text("Module (e.g. Module 1, Battle)") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
            )
        }

        successMessage?.let {
            Text(it, color = EmeraldTeal, fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 4.dp))
        }

        Button(
            onClick = {
                if (qText.isBlank() || optA.isBlank() || optB.isBlank() || correctOpt.isBlank()) {
                    successMessage = "Error: Question, Options, and Correct Answer are required."
                } else {
                    viewModel.addCustomQuestion(
                        text = qText,
                        options = listOf(optA, optB, optC, optD).filter { it.isNotBlank() },
                        correctAnswer = correctOpt,
                        explanation = explanationText.ifBlank { "Verified historical fact." },
                        category = categoryText,
                        module = moduleText,
                        difficulty = difficultyText
                    )
                    successMessage = "Masha'Allah! Question saved to local pool!"
                    qText = ""
                    optA = ""
                    optB = ""
                    optC = ""
                    optD = ""
                    correctOpt = ""
                    explanationText = ""
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("admin_save_button")
        ) {
            Text("Save Question to Pool", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// === TAB 2: AI MCQ Generator View ===
@Composable
fun AiGeneratorView(viewModel: SeerahViewModel) {
    var category by remember { mutableStateOf("Birth") }
    var difficulty by remember { mutableStateOf("Medium") }
    val isGenerating by viewModel.isGeneratingMcq.collectAsState()
    val generatedQ by viewModel.generatedMcq.collectAsState()

    var statusMessage by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            "AI MCQ Generator",
            style = MaterialTheme.typography.titleMedium.copy(color = RadiantGold, fontWeight = FontWeight.Bold)
        )
        Text(
            "Query the Gemini-3.5-flash model to automatically create authentic Seerah questions with complete explanations.",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp
        )

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Topic/Category") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { difficulty = "Easy" },
                colors = ButtonDefaults.buttonColors(containerColor = if (difficulty == "Easy") RadiantGold else CelestialBlue),
                modifier = Modifier.weight(1f)
            ) { Text("Easy") }

            Button(
                onClick = { difficulty = "Medium" },
                colors = ButtonDefaults.buttonColors(containerColor = if (difficulty == "Medium") RadiantGold else CelestialBlue),
                modifier = Modifier.weight(1f)
            ) { Text("Medium") }

            Button(
                onClick = { difficulty = "Hard" },
                colors = ButtonDefaults.buttonColors(containerColor = if (difficulty == "Hard") RadiantGold else CelestialBlue),
                modifier = Modifier.weight(1f)
            ) { Text("Hard") }
        }

        Button(
            onClick = {
                statusMessage = null
                viewModel.generateQuestionWithAi(category, difficulty)
            },
            enabled = !isGenerating,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("ai_generate_submit")
        ) {
            if (isGenerating) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text("Generate MCQ with Gemini AI", fontWeight = FontWeight.Bold)
            }
        }

        // Preview generated question
        generatedQ?.let { q ->
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("AI PREVIEW", color = RadiantGold, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Text("Q: ${q.text}", fontWeight = FontWeight.Bold)
                    q.getOptionsList().forEach { opt ->
                        Text("• $opt", fontSize = 14.sp)
                    }
                    Text("Correct: ${q.correctAnswer}", color = EmeraldTeal, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Exp: ${q.explanation}", fontSize = 13.sp, color = Color.Gray)

                    Button(
                        onClick = {
                            viewModel.saveGeneratedQuestionToDb()
                            statusMessage = "Question successfully saved into local database pool!"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                        modifier = Modifier.fillMaxWidth().testTag("ai_generate_save")
                    ) {
                        Text("Save & Add to Database")
                    }
                }
            }
        }

        statusMessage?.let {
            Text(it, color = EmeraldTeal, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// === TAB 3: Batch/Excel JSON Import ===
@Composable
fun BatchImportView(viewModel: SeerahViewModel, currentSize: Int) {
    var rawText by remember { mutableStateOf("") }
    var importStatus by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Import Questions via CSV / JSON", style = MaterialTheme.typography.titleMedium.copy(color = RadiantGold, fontWeight = FontWeight.Bold))
        Text(
            "Simulate Excel/CSV import by pasting a comma-separated row text. Paste text in this format:\n" +
                    "Question text | Opt 1, Opt 2, Opt 3, Opt 4 | Correct Opt | Explanation | Category | Module",
            color = Color.White.copy(alpha = 0.7f),
            fontSize = 13.sp
        )

        OutlinedTextField(
            value = rawText,
            onValueChange = { rawText = it },
            placeholder = { Text("Who was Prophet's father? | Abdullah, Abu Talib, Hamza | Abdullah | Abdullah passed away early. | Family Tree | Module 1") },
            modifier = Modifier.fillMaxWidth().height(150.dp).testTag("admin_import_text"),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White)
        )

        Button(
            onClick = {
                try {
                    val parts = rawText.split("|").map { it.trim() }
                    if (parts.size >= 6) {
                        val text = parts[0]
                        val options = parts[1].split(",").map { it.trim() }
                        val correct = parts[2]
                        val exp = parts[3]
                        val cat = parts[4]
                        val mod = parts[5]

                        viewModel.addCustomQuestion(text, options, correct, exp, cat, mod, "Medium")
                        importStatus = "Success! Loaded row and inserted into Database."
                        rawText = ""
                    } else {
                        importStatus = "Error: Invalid row format. Please make sure to separate with '|'"
                    }
                } catch (e: Exception) {
                    importStatus = "Import Error: ${e.message}"
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
            modifier = Modifier.fillMaxWidth().height(50.dp).testTag("admin_import_submit")
        ) {
            Text("Simulate Excel Import", fontWeight = FontWeight.Bold)
        }

        importStatus?.let {
            Text(it, color = if (it.startsWith("Success")) EmeraldTeal else Color.Red, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// === TAB 4: Student Analytics View ===
@Composable
fun AnalyticsView(
    totalQuestions: Int,
    customCount: Int,
    historyList: List<com.example.data.model.QuizHistory>
) {
    val totalRuns = historyList.size
    val averageAccuracy = if (totalRuns > 0) historyList.map { it.percentage }.average() else 0.0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Performance & Database Metrics", style = MaterialTheme.typography.titleMedium.copy(color = RadiantGold, fontWeight = FontWeight.Bold))

        // Analytics Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                AnalyticsRow("Total Question Pool Size", "$totalQuestions MCQs")
                AnalyticsRow("Custom/AI Generated Added", "$customCount MCQs")
                AnalyticsRow("Total Student Quiz Attempts", "$totalRuns")
                AnalyticsRow("Average Classroom Accuracy", "${String.format("%.1f", averageAccuracy)}%")
            }
        }

        Text("Active Students Breakdown", style = MaterialTheme.typography.titleMedium.copy(color = RadiantGold, fontWeight = FontWeight.Bold))

        // Student Table
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                StudentRow("Student Name", "Level Rank", "Class score", isHeader = true)
                StudentRow("Guest Student (You)", "Scholar", "$totalQuestions facts", isHeader = false)
                StudentRow("Aisha Siddiqa", "Seerah Master", "9800 pts", isHeader = false)
                StudentRow("Zayd Al-Hasan", "Seerah Master", "7100 pts", isHeader = false)
                StudentRow("Khadijah Khan", "Scholar", "6500 pts", isHeader = false)
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AnalyticsRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = Color.Gray, fontSize = 14.sp)
        Text(value, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
    }
}

@Composable
fun StudentRow(name: String, level: String, score: String, isHeader: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(name, fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Medium, color = if (isHeader) RadiantGold else MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1.5f), fontSize = 13.sp)
        Text(level, fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Normal, color = if (isHeader) RadiantGold else Color.Gray, modifier = Modifier.weight(1.2f), fontSize = 13.sp)
        Text(score, fontWeight = if (isHeader) FontWeight.Bold else FontWeight.Bold, color = if (isHeader) RadiantGold else EmeraldTeal, modifier = Modifier.weight(1f), textAlign = TextAlign.End, fontSize = 13.sp)
    }
}
