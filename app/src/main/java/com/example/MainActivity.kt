package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.SeerahViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                val viewModel: SeerahViewModel = viewModel()
                var loggedInUsername by remember { mutableStateOf<String?>(null) }
                val stats by viewModel.userStats.collectAsState()

                NavHost(
                    navController = navController,
                    startDestination = "auth"
                ) {
                    composable("auth") {
                        AuthScreen(
                            onAuthSuccess = { username ->
                                loggedInUsername = username
                                navController.navigate("dashboard") {
                                    popUpTo("auth") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("dashboard") {
                        val currentUsername = loggedInUsername ?: "Guest"
                        DashboardScreen(
                            username = currentUsername,
                            userStats = stats,
                            onNavigateToQuiz = { module, category, mode ->
                                viewModel.startQuiz(module, category, mode)
                                navController.navigate("quiz")
                            },
                            onNavigateToTutor = { navController.navigate("tutor") },
                            onNavigateToLeaderboard = { navController.navigate("leaderboard") },
                            onNavigateToCertificates = { navController.navigate("certificates") },
                            onNavigateToAdmin = { navController.navigate("admin") },
                            onLogout = {
                                loggedInUsername = null
                                navController.navigate("auth") {
                                    popUpTo("dashboard") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("quiz") {
                        QuizScreen(
                            viewModel = viewModel,
                            onNavigateToResults = {
                                navController.navigate("results") {
                                    popUpTo("quiz") { inclusive = true }
                                }
                            },
                            onNavigateBack = {
                                navController.popBackStack()
                            }
                        )
                    }

                    composable("results") {
                        ResultsScreen(
                            viewModel = viewModel,
                            onNavigateBackToDashboard = {
                                navController.navigate("dashboard") {
                                    popUpTo("results") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("tutor") {
                        TutorScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable("leaderboard") {
                        LeaderboardScreen(
                            viewModel = viewModel,
                            username = loggedInUsername ?: "Guest",
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable("certificates") {
                        CertificatesScreen(
                            viewModel = viewModel,
                            username = loggedInUsername ?: "Guest",
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable("admin") {
                        AdminScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
