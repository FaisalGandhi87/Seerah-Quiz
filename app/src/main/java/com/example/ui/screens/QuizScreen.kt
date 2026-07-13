package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun QuizScreen(
    viewModel: SeerahViewModel,
    onNavigateToResults: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val questions by viewModel.quizQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val selectedAns by viewModel.selectedAnswer.collectAsState()
    val isChecked by viewModel.isAnswerChecked.collectAsState()
    val score by viewModel.quizScore.collectAsState()
    val streak by viewModel.quizStreak.collectAsState()
    val timeSec by viewModel.quizTimeSeconds.collectAsState()
    val mode by viewModel.quizMode.collectAsState()
    val category by viewModel.quizCategory.collectAsState()
    val module by viewModel.quizModule.collectAsState()
    val isFinished by viewModel.isQuizFinished.collectAsState()

    var showVoiceLangMenu by remember { mutableStateOf(false) }

    // Redirect to results once finished
    LaunchedEffect(isFinished) {
        if (isFinished) {
            onNavigateToResults()
        }
    }

    val currentQuestion = questions.getOrNull(currentIndex)

    // Formatted time (MM:SS)
    val minutes = timeSec / 60
    val seconds = timeSec % 60
    val timeFormatted = String.format("%02d:%02d", minutes, seconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "$module - $category",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.stopSpeaking()
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    // Voice Quiz Speaker button (reads question aloud in Urdu, Arabic, English)
                    IconButton(onClick = { showVoiceLangMenu = true }) {
                        Icon(Icons.Default.VolumeUp, contentDescription = "Read Question Aloud", tint = RadiantGold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepNavy)
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        if (currentQuestion == null) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = EmeraldTeal)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Loading interactive question bank...", color = Color.White)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                // Progress Bar and Stats Banner
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${currentIndex + 1} of ${questions.size}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (mode == "Exam" || mode == "Battle" || mode == "Survival") {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = AmberGold, modifier = Modifier.size(16.dp))
                            Text(text = timeFormatted, color = AmberGold, fontWeight = FontWeight.Bold)
                        }

                        Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                        Text(text = "Streak: $streak", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold)
                    }
                }

                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size.toFloat() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = EmeraldTeal,
                    trackColor = Color.White.copy(alpha = 0.1f)
                )

                // Question Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentQuestion.text,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Options List
                val options = currentQuestion.getOptionsList()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    options.forEachIndexed { index, option ->
                        val isSelected = selectedAns == option
                        val isCorrect = option == currentQuestion.correctAnswer

                        // Color logic based on checked state and mode
                        val (bgColor, borderColor, textColor) = when {
                            isChecked && mode == "Practice" -> {
                                when {
                                    isCorrect -> Triple(EmeraldTeal.copy(alpha = 0.15f), EmeraldTeal, EmeraldTeal)
                                    isSelected -> Triple(Color.Red.copy(alpha = 0.15f), Color.Red, Color.Red)
                                    else -> Triple(MaterialTheme.colorScheme.surface, Color.Transparent, MaterialTheme.colorScheme.onSurface)
                                }
                            }
                            isSelected -> Triple(CelestialBlue.copy(alpha = 0.15f), CelestialBlue, CelestialBlue)
                            else -> Triple(MaterialTheme.colorScheme.surface, Color.Transparent, MaterialTheme.colorScheme.onSurface)
                        }

                        Card(
                            onClick = { viewModel.selectAnswer(option) },
                            colors = CardDefaults.cardColors(containerColor = bgColor),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                                .testTag("option_${index + 1}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (isSelected) CelestialBlue else MaterialTheme.colorScheme.background,
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = ('A'.code + index).toChar().toString(),
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Text(
                                    text = option,
                                    color = textColor,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 15.sp
                                )

                                if (isChecked && mode == "Practice") {
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (isCorrect) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = EmeraldTeal)
                                    } else if (isSelected) {
                                        Icon(Icons.Default.Cancel, contentDescription = "Wrong", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }

                    // Explanation Card in Practice Mode
                    AnimatedVisibility(
                        visible = isChecked && mode == "Practice",
                        enter = expandVertically() + fadeIn(),
                        exit = shrinkVertically() + fadeOut()
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = EmeraldTeal.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldTeal)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Explanation & Reference", fontWeight = FontWeight.Bold, color = EmeraldTeal, fontSize = 14.sp)
                                }
                                Text(
                                    text = currentQuestion.explanation,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Submit / Next Button
                Button(
                    onClick = {
                        if (isChecked && mode == "Practice") {
                            viewModel.moveToNextQuestion()
                        } else {
                            viewModel.submitAnswer()
                        }
                    },
                    enabled = selectedAns != null,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("quiz_submit_button")
                ) {
                    Text(
                        text = if (isChecked && mode == "Practice") "Next Question" else "Submit Answer",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    // Voice language menu dialog
    if (showVoiceLangMenu) {
        AlertDialog(
            onDismissRequest = { showVoiceLangMenu = false },
            title = { Text("Choose Voice Language", fontWeight = FontWeight.Bold, color = EmeraldTeal, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Select a language to read this question aloud:", fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))

                    Button(
                        onClick = {
                            currentQuestion?.let { viewModel.speakText(it.text, "en") }
                            showVoiceLangMenu = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("English Voice Synthesis")
                    }

                    Button(
                        onClick = {
                            currentQuestion?.let { viewModel.speakText(it.text, "ur") }
                            showVoiceLangMenu = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Urdu Voice Synthesis")
                    }

                    Button(
                        onClick = {
                            currentQuestion?.let { viewModel.speakText(it.text, "ar") }
                            showVoiceLangMenu = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CelestialBlue),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Arabic Voice Synthesis")
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showVoiceLangMenu = false }) {
                    Text("Close", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}
