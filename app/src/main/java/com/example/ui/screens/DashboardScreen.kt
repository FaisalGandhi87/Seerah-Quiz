package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.UserStats
import com.example.ui.theme.AmberGold
import com.example.ui.theme.CelestialBlue
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.RadiantGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    username: String,
    userStats: UserStats?,
    onNavigateToQuiz: (module: String, category: String, mode: String) -> Unit,
    onNavigateToTutor: () -> Unit,
    onNavigateToLeaderboard: () -> Unit,
    onNavigateToCertificates: () -> Unit,
    onNavigateToAdmin: () -> Unit,
    onLogout: () -> Unit
) {
    val stats = userStats ?: UserStats()

    // Modules list
    val mainModules = listOf(
        SeerahModuleInfo("Module 1", "Early Life", "Birth, Family Tree, Childhood, Halimah (RA), Youth, Khadijah (RA)", Icons.Default.ChildCare),
        SeerahModuleInfo("Module 2", "Prophethood", "Cave Hira, First Revelation, Secret & Public Dawah, Taif, Boycott", Icons.Default.MenuBook),
        SeerahModuleInfo("Module 3", "Hijrah & Madinah", "Migration, Masjid Quba, Brotherhood pact, Constitution of Madinah", Icons.Default.Map),
        SeerahModuleInfo("Module 4", "Madinan Period", "Jewish Tribes, Treaties, Delegations, Social reforms", Icons.Default.LocationCity),
        SeerahModuleInfo("Module 5", "Farewell Period", "Conquest of Makkah, Farewell Hajj, Last Sermon, Final days", Icons.Default.AccountBalance)
    )

    // Battles list
    val battles = listOf(
        BattleInfo(1, "Badr", "2 AH - The Great Battle", Icons.Default.Shield),
        BattleInfo(2, "Uhud", "3 AH - The Archers' Test", Icons.Default.Shield),
        BattleInfo(3, "Khandaq", "5 AH - Trench Defense", Icons.Default.Shield),
        BattleInfo(4, "Banu Qaynuqa", "2 AH - First Expatriation", Icons.Default.Shield),
        BattleInfo(5, "Banu Nadir", "4 AH - Second Fortress Siege", Icons.Default.Shield),
        BattleInfo(6, "Banu Qurayzah", "5 AH - Treaty violation verdict", Icons.Default.Shield),
        BattleInfo(7, "Khaybar", "7 AH - High Fortresses", Icons.Default.Shield),
        BattleInfo(8, "Mu'tah", "8 AH - Byzantine clash", Icons.Default.Shield),
        BattleInfo(9, "Hunayn", "8 AH - Steep valley ambush", Icons.Default.Shield),
        BattleInfo(10, "Tabuk", "9 AH - Expedition without fight", Icons.Default.Shield)
    )

    var showQuizModeDialog by remember { mutableStateOf<Pair<String, String>?>(null) } // Module, Category

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Seerah Challenge Pro",
                            fontWeight = FontWeight.Bold,
                            color = RadiantGold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = "Welcome, $username",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToAdmin, modifier = Modifier.testTag("admin_nav")) {
                        Icon(Icons.Default.Settings, contentDescription = "Admin Panel", tint = RadiantGold)
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepNavy,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DeepNavy
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hero Banner and User Stats Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_seerah_hero),
                        contentDescription = "Seerah banner",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                )
                            )
                    )

                    // Stats Info Overlay
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.WorkspacePremium, contentDescription = "Level", tint = RadiantGold, modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = stats.level,
                                color = RadiantGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Text(
                            text = "${stats.points} Points  •  Streak: ${stats.currentStreak} 🔥  •  Unlocks: ${stats.battlesUnlockedCount}/10 🛡️",
                            color = Color.White,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // Quick Nav Shortcuts
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // AI Tutor
                    Card(
                        onClick = onNavigateToTutor,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f).height(90.dp).testTag("ai_tutor_shortcut")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = "AI Tutor", tint = EmeraldTeal, modifier = Modifier.size(28.dp))
                            Text("AI Tutor", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }

                    // Leaderboards
                    Card(
                        onClick = onNavigateToLeaderboard,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f).height(90.dp).testTag("leaderboard_shortcut")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Leaderboard, contentDescription = "Leaderboard", tint = AmberGold, modifier = Modifier.size(28.dp))
                            Text("Ranks", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }

                    // Certificates
                    Card(
                        onClick = onNavigateToCertificates,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.weight(1f).height(90.dp).testTag("certificates_shortcut")
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.CardMembership, contentDescription = "Certificates", tint = RadiantGold, modifier = Modifier.size(28.dp))
                            Text("Certificates", fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }

            // Special Challenge Modes
            item {
                Column {
                    Text(
                        text = "Special Challenge Modes",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Daily Quiz Card
                        Card(
                            onClick = { onNavigateToQuiz("Daily", "All", "Daily") },
                            colors = CardDefaults.cardColors(containerColor = CelestialBlue),
                            modifier = Modifier.weight(1f).height(100.dp).testTag("daily_quiz_card")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Icon(Icons.Default.Today, contentDescription = null, tint = RadiantGold)
                                    Badge(containerColor = AmberGold) { Text("NEW", color = DeepNavy, fontWeight = FontWeight.Bold) }
                                }
                                Column {
                                    Text("Daily Quiz", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                    Text("30 random MCQs", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                }
                            }
                        }

                        // Survival Mode Card
                        Card(
                            onClick = { onNavigateToQuiz("Survival", "All", "Survival") },
                            colors = CardDefaults.cardColors(containerColor = EmeraldTeal),
                            modifier = Modifier.weight(1f).height(100.dp).testTag("survival_quiz_card")
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize().padding(14.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.Red)
                                Column {
                                    Text("Survival Mode", fontWeight = FontWeight.Bold, color = Color.White, fontSize = 14.sp)
                                    Text("Wrong Answer = Over", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Main Seerah Study Modules
            item {
                Text(
                    text = "Core Seerah Modules",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(mainModules) { mod ->
                Card(
                    onClick = { showQuizModeDialog = Pair(mod.id, "All") },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = EmeraldTeal.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.size(50.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(mod.icon, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(26.dp))
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = mod.id,
                                color = RadiantGold,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = mod.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = mod.topics,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        Icon(
                            Icons.Default.ChevronRight,
                            contentDescription = "Go",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }

            // Battle Modules Section (Unlocked progressively)
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Historical Battles (Challenge)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "${stats.battlesUnlockedCount} Unlocked",
                            style = MaterialTheme.typography.bodySmall.copy(color = RadiantGold)
                        )
                    }

                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(battles) { battle ->
                            val isUnlocked = battle.levelNum <= stats.battlesUnlockedCount

                            Card(
                                onClick = {
                                    if (isUnlocked) {
                                        showQuizModeDialog = Pair("Battle", battle.name)
                                    }
                                },
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.4f)
                                ),
                                shape = RoundedCornerShape(16.dp),
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(130.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(14.dp),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                battle.icon,
                                                contentDescription = null,
                                                tint = if (isUnlocked) EmeraldTeal else Color.Gray,
                                                modifier = Modifier.size(22.dp)
                                            )
                                            Text(
                                                text = "LV ${battle.levelNum}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) RadiantGold else Color.Gray
                                            )
                                        }

                                        Column {
                                            Text(
                                                text = battle.name,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                                fontSize = 15.sp,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Text(
                                                text = battle.shortDesc,
                                                fontSize = 11.sp,
                                                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }

                                    if (!isUnlocked) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.4f)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.Lock,
                                                contentDescription = "Locked",
                                                tint = Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // Quiz Mode Selection Dialog (Practice or Exam)
    showQuizModeDialog?.let { pair ->
        AlertDialog(
            onDismissRequest = { showQuizModeDialog = null },
            title = {
                Text(
                    text = "Select Study Mode",
                    fontWeight = FontWeight.Bold,
                    color = EmeraldTeal,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Choose how you want to challenge yourself for ${pair.first} - ${pair.second}:",
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Practice Mode Button
                    Card(
                        onClick = {
                            onNavigateToQuiz(pair.first, pair.second, "Practice")
                            showQuizModeDialog = null
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = EmeraldTeal)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Practice Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Unlimited attempts, instant answers & explanations.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }

                    // Exam Mode Button
                    Card(
                        onClick = {
                            onNavigateToQuiz(pair.first, pair.second, "Exam")
                            showQuizModeDialog = null
                        },
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Timer, contentDescription = null, tint = AmberGold)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Exam Mode", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text("Timer enabled, no hints, final scoreboard.", fontSize = 11.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showQuizModeDialog = null }) {
                    Text("Cancel", color = Color.Gray)
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}

data class SeerahModuleInfo(
    val id: String,
    val title: String,
    val topics: String,
    val icon: ImageVector
)

data class BattleInfo(
    val levelNum: Int,
    val name: String,
    val shortDesc: String,
    val icon: ImageVector
)
