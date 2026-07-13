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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
fun ResultsScreen(
    viewModel: SeerahViewModel,
    onNavigateBackToDashboard: () -> Unit
) {
    val score by viewModel.quizScore.collectAsState()
    val correct by viewModel.correctAnswersCount.collectAsState()
    val wrong by viewModel.wrongAnswersCount.collectAsState()
    val timeSec by viewModel.quizTimeSeconds.collectAsState()
    val mode by viewModel.quizMode.collectAsState()
    val category by viewModel.quizCategory.collectAsState()
    val module by viewModel.quizModule.collectAsState()
    val weakAreas by viewModel.weakAreas.collectAsState()
    val suggestions by viewModel.suggestedRevisions.collectAsState()
    val userStats by viewModel.userStats.collectAsState()

    val totalQ = if (mode == "Survival") correct + wrong else correct + wrong
    val percentage = if (totalQ > 0) (correct.toDouble() / totalQ.toDouble()) * 100.0 else 0.0

    // Compute certificate level
    val certificateTitle = when {
        percentage >= 100.0 -> "Seerah Master Certificate"
        percentage >= 90.0 -> "Gold Certificate"
        percentage >= 75.0 -> "Silver Certificate"
        percentage >= 60.0 -> "Bronze Certificate"
        else -> null
    }

    val certificateColor = when {
        percentage >= 100.0 -> RadiantGold
        percentage >= 90.0 -> AmberGold
        percentage >= 75.0 -> Color(0xFFC0C0C0) // Silver
        percentage >= 60.0 -> Color(0xFFCD7F32) // Bronze
        else -> Color.Gray
    }

    val min = timeSec / 60
    val sec = timeSec % 60
    val timeStr = "${min}m ${sec}s"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Seerah Challenge Completed", fontWeight = FontWeight.Bold, color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DeepNavy)
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Success Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (percentage >= 60.0) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (percentage >= 60.0) EmeraldTeal else AmberGold,
                        modifier = Modifier.size(64.dp)
                    )

                    Text(
                        text = if (percentage >= 60.0) "Masha'Allah! Completed!" else "Keep Learning Seerah!",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Text(
                        text = "$module • $category",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Key Stats Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem(label = "Score", value = "+$score pts")
                        StatItem(label = "Correct", value = "$correct")
                        StatItem(label = "Wrong", value = "$wrong")
                        StatItem(label = "Time", value = timeStr)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Accuracy: ${String.format("%.1f", percentage)}%",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold, color = EmeraldTeal)
                    )
                }
            }

            // Certificate Unlocked Card (Requested Certificate Generation)
            certificateTitle?.let { certTitle ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = 3.dp,
                            brush = Brush.sweepGradient(
                                colors = listOf(RadiantGold, AmberGold, RadiantGold)
                            ),
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.CardMembership,
                            contentDescription = "Certificate Unlocked",
                            tint = certificateColor,
                            modifier = Modifier.size(56.dp)
                        )

                        Text(
                            text = "OFFICIAL SEERAH CERTIFICATE",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = RadiantGold,
                            letterSpacing = 1.5.sp,
                            modifier = Modifier.padding(top = 12.dp)
                        )

                        Text(
                            text = certTitle,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = "Awarded to Guest Student for demonstrating high competency in Prophet Muhammad's ﷺ life history with an accuracy of ${String.format("%.1f", percentage)}%.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(16.dp))
                            Text("Verified by Seerah Challenge Pro", fontSize = 11.sp, color = EmeraldTeal)
                        }
                    }
                }
            }

            // Performance Analysis & Suggestions (Suggested Revision Topics)
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Weak Areas & Revision suggestions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (weakAreas.isEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldTeal)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Mastery demonstrated! No weak areas spotted in this run.", fontSize = 13.sp, color = Color.Gray)
                        }
                    } else {
                        Text(
                            text = "Weak Areas: " + weakAreas.joinToString(", "),
                            color = CoralColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        suggestions.forEach { suggestion ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(Icons.Default.MenuBook, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(16.dp).padding(top = 2.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(suggestion, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                            }
                        }
                    }
                }
            }

            // Return Button
            Button(
                onClick = onNavigateBackToDashboard,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldTeal),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("results_exit_button")
            ) {
                Text("Return to Dashboard", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Text(text = value, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.padding(top = 4.dp))
    }
}

val CoralColor = Color(0xFFD94E34)
