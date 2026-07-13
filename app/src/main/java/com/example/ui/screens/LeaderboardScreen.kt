package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Star
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
fun LeaderboardScreen(
    viewModel: SeerahViewModel,
    username: String,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val userStats by viewModel.userStats.collectAsState()
    val points = userStats?.points ?: 0

    val tabs = listOf("Daily", "Weekly", "Monthly", "Global", "Madrasa")

    // Mock scholars representing other competitors
    val leaders = when (selectedTab) {
        0 -> listOf(
            Competitor("Zayd Al-Hasan", 540, "Scholar", true),
            Competitor("Khadijah Khan", 480, "Scholar", false),
            Competitor("Yusuf Siddiqui", 410, "Talib-e-Ilm", false),
            Competitor(username, points, userStats?.level ?: "Student", false),
            Competitor("Fatima Zahra", 320, "Talib-e-Ilm", false),
            Competitor("Hamza Tariq", 280, "Student", false)
        )
        1 -> listOf(
            Competitor("Aisha Siddiqa", 2100, "Muhaddith", false),
            Competitor("Umar Farooq", 1950, "Scholar", false),
            Competitor("Zayd Al-Hasan", 1880, "Scholar", false),
            Competitor("Khadijah Khan", 1750, "Scholar", false),
            Competitor(username, points, userStats?.level ?: "Student", false),
            Competitor("Ali Ibn Abi", 1400, "Scholar", false)
        )
        2 -> listOf(
            Competitor("Aisha Siddiqa", 4500, "Seerah Expert", false),
            Competitor("Abdur Rahman", 4100, "Seerah Expert", false),
            Competitor("Umar Farooq", 3900, "Scholar", false),
            Competitor(username, points, userStats?.level ?: "Student", false),
            Competitor("Zayd Al-Hasan", 3200, "Scholar", false),
            Competitor("Bilal Habashi", 2900, "Scholar", false)
        )
        3 -> listOf(
            Competitor("Aisha Siddiqa", 9800, "Seerah Master", false),
            Competitor("Abdur Rahman", 8900, "Seerah Master", false),
            Competitor("Umar Farooq", 7800, "Seerah Master", false),
            Competitor("Zayd Al-Hasan", 7100, "Seerah Master", false),
            Competitor("Khadijah Khan", 6500, "Seerah Expert", false),
            Competitor(username, points, userStats?.level ?: "Student", false)
        )
        else -> listOf( // Madrasa ranking
            Competitor("Muadh Al-Qari", 1200, "Scholar", false),
            Competitor("Sumayyah Yasir", 1080, "Scholar", false),
            Competitor(username, points, userStats?.level ?: "Student", false),
            Competitor("Anas Ibn Malik", 880, "Scholar", false),
            Competitor("Fatima Zahra", 640, "Talib-e-Ilm", false)
        )
    }.sortedByDescending { it.points }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Leaderboard & Ranks", fontWeight = FontWeight.Bold, color = Color.White) },
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
            // Ranking Filter Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = DeepNavy,
                contentColor = RadiantGold,
                edgePadding = 0.dp,
                modifier = Modifier.fillMaxWidth().testTag("leaderboard_tabs")
            ) {
                tabs.forEachIndexed { idx, tabTitle ->
                    Tab(
                        selected = selectedTab == idx,
                        onClick = { selectedTab = idx },
                        text = { Text(tabTitle, fontWeight = FontWeight.Bold) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Top Scholar Highlights (Podium style list)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                itemsIndexed(leaders) { idx, competitor ->
                    val isCurrentUser = competitor.name == username
                    val rankNum = idx + 1

                    val itemBgColor = when {
                        isCurrentUser -> EmeraldTeal.copy(alpha = 0.15f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val medalColor = when (rankNum) {
                        1 -> RadiantGold
                        2 -> Color(0xFFC0C0C0) // Silver
                        3 -> Color(0xFFCD7F32) // Bronze
                        else -> Color.Gray
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = itemBgColor),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isCurrentUser) 4.dp else 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = if (isCurrentUser) 2.dp else 0.dp,
                                color = if (isCurrentUser) EmeraldTeal else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Rank Number / Trophy
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.size(36.dp)
                            ) {
                                if (rankNum <= 3) {
                                    Icon(
                                        Icons.Default.EmojiEvents,
                                        contentDescription = "Medal",
                                        tint = medalColor,
                                        modifier = Modifier.size(28.dp)
                                    )
                                } else {
                                    Text(
                                        text = "$rankNum",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Gray,
                                        fontSize = 15.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            // Competitor Name and Level Badge
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = competitor.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp,
                                        color = if (isCurrentUser) EmeraldTeal else MaterialTheme.colorScheme.onSurface
                                    )
                                    if (isCurrentUser) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Badge(containerColor = EmeraldTeal) { Text("YOU", color = Color.White, fontSize = 9.sp) }
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 2.dp)
                                ) {
                                    Icon(Icons.Default.School, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(12.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = competitor.level,
                                        fontSize = 11.sp,
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Score points
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, contentDescription = null, tint = RadiantGold, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${competitor.points} pts",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class Competitor(
    val name: String,
    val points: Int,
    val level: String,
    val isOnline: Boolean
)
