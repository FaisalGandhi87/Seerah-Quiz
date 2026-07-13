package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
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
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldTeal
import com.example.ui.theme.RadiantGold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CertificatesScreen(
    viewModel: SeerahViewModel,
    username: String,
    onNavigateBack: () -> Unit
) {
    val userStats by viewModel.userStats.collectAsState()
    val stats = userStats ?: com.example.data.model.UserStats()

    val certList = listOf(
        CertificateDetails(
            title = "Bronze Certificate",
            requirement = "Achieve 60% accuracy or higher on any quiz.",
            isUnlocked = stats.bronzeCertificateEarned,
            color = Color(0xFFCD7F32) // Bronze
        ),
        CertificateDetails(
            title = "Silver Certificate",
            requirement = "Achieve 75% accuracy or higher on any quiz.",
            isUnlocked = stats.silverCertificateEarned,
            color = Color(0xFFC0C0C0) // Silver
        ),
        CertificateDetails(
            title = "Gold Certificate",
            requirement = "Achieve 90% accuracy or higher on any quiz.",
            isUnlocked = stats.goldCertificateEarned,
            color = AmberGold
        ),
        CertificateDetails(
            title = "Seerah Master Certificate",
            requirement = "Achieve 100% perfect score on any core quiz.",
            isUnlocked = stats.masterCertificateEarned,
            color = RadiantGold
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Certificates", fontWeight = FontWeight.Bold, color = Color.White) },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(
                    text = "Demonstrate your knowledge of Prophet Muhammad's ﷺ life to unlock prestigious verifiable certificates.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.7f)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(certList.size) { index ->
                val cert = certList[index]

                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (cert.isUnlocked) 2.dp else 0.dp,
                            color = if (cert.isUnlocked) cert.color else Color.Transparent,
                            shape = RoundedCornerShape(24.dp)
                        )
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.CardMembership,
                                contentDescription = null,
                                tint = if (cert.isUnlocked) cert.color else Color.Gray.copy(alpha = 0.5f),
                                modifier = Modifier.size(56.dp)
                            )

                            Text(
                                text = "OFFICIAL CERTIFICATE",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (cert.isUnlocked) RadiantGold else Color.Gray,
                                letterSpacing = 2.sp,
                                modifier = Modifier.padding(top = 12.dp)
                            )

                            Text(
                                text = cert.title,
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = if (cert.isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            Text(
                                text = if (cert.isUnlocked) {
                                    "Successfully awarded to $username for proving high capability in Prophet Muhammad's ﷺ life."
                                } else {
                                    "Locked: ${cert.requirement}"
                                },
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = if (cert.isUnlocked) 0.7f else 0.4f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(top = 8.dp)
                            )

                            if (cert.isUnlocked) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldTeal, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Verified", fontSize = 11.sp, color = EmeraldTeal)
                                    }

                                    IconButton(
                                        onClick = {},
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Icon(Icons.Default.Share, contentDescription = "Share", tint = EmeraldTeal, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }

                        if (!cert.isUnlocked) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Black.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = "Locked",
                                    tint = Color.White.copy(alpha = 0.4f),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

data class CertificateDetails(
    val title: String,
    val requirement: String,
    val isUnlocked: Boolean,
    val color: Color
)
