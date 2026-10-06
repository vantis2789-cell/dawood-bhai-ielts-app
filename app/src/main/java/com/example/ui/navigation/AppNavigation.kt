package com.example.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IeltsModuleType
import com.example.ui.screens.about.AboutScreen
import com.example.ui.screens.admin.AdminDashboardScreen
import com.example.ui.screens.auth.AuthScreen
import com.example.ui.screens.grammar.GrammarLabScreen
import com.example.ui.screens.history.TestHistoryScreen
import com.example.ui.screens.home.HomeScreen
import com.example.ui.screens.learn.LearnScreen
import com.example.ui.screens.listening.ListeningPracticeScreen
import com.example.ui.screens.mocktest.MockTestCenterScreen
import com.example.ui.screens.mocktest.MockTestRunnerScreen
import com.example.ui.screens.onboarding.OnboardingScreen
import com.example.ui.screens.profile.ProfileScreen
import com.example.ui.screens.reading.ReadingPracticeScreen
import com.example.ui.screens.result.ResultScreen
import com.example.ui.screens.review.QuestionReviewScreen
import com.example.ui.screens.speaking.AiSpeakingPracticeScreen
import com.example.ui.screens.speaking.SpeakingPracticeScreen
import com.example.ui.screens.writing.WritingPracticeScreen
import com.example.ui.screens.splash.SplashScreen
import com.example.ui.screens.teacher.TeacherDashboardScreen
import com.example.ui.screens.vocab.VocabLabScreen
import com.example.ui.theme.*

enum class AppDestination {
    SPLASH,
    ONBOARDING,
    AUTH,
    MAIN_HOME,
    MAIN_LEARN,
    MAIN_TESTS,
    MAIN_VOCAB,
    MAIN_PROFILE,
    LISTENING_PRACTICE,
    READING_PRACTICE,
    WRITING_PRACTICE,
    SPEAKING_PRACTICE,
    AI_SPEAKING_PRACTICE,
    MOCK_TEST_RUNNER,
    RESULT_ANALYSIS,
    TEST_HISTORY,
    QUESTION_REVIEW,
    GRAMMAR_LAB,
    TEACHER_DASHBOARD,
    ADMIN_DASHBOARD,
    ABOUT
}

@Composable
fun AppNavigation() {
    var currentDestination by remember { mutableStateOf(AppDestination.SPLASH) }
    var previousDestination by remember { mutableStateOf(AppDestination.MAIN_HOME) }
    var resultScoreOverride by remember { mutableStateOf<Double?>(null) }
    var activeRunningTestId by remember { mutableStateOf("test_ac_01") }
    var activeReviewAttemptId by remember { mutableStateOf<String?>(null) }

    fun navigateTo(dest: AppDestination) {
        previousDestination = currentDestination
        currentDestination = dest
    }

    // Determine if we should show bottom navigation bar
    val isBottomBarVisible = currentDestination in listOf(
        AppDestination.MAIN_HOME,
        AppDestination.MAIN_LEARN,
        AppDestination.MAIN_TESTS,
        AppDestination.MAIN_VOCAB,
        AppDestination.MAIN_PROFILE
    )

    // Back button handling
    BackHandler(enabled = currentDestination != AppDestination.MAIN_HOME && currentDestination != AppDestination.SPLASH) {
        when (currentDestination) {
            AppDestination.ONBOARDING -> navigateTo(AppDestination.SPLASH)
            AppDestination.AUTH -> navigateTo(AppDestination.ONBOARDING)
            AppDestination.MAIN_LEARN,
            AppDestination.MAIN_TESTS,
            AppDestination.MAIN_VOCAB,
            AppDestination.MAIN_PROFILE -> navigateTo(AppDestination.MAIN_HOME)
            AppDestination.LISTENING_PRACTICE,
            AppDestination.READING_PRACTICE,
            AppDestination.WRITING_PRACTICE,
            AppDestination.SPEAKING_PRACTICE,
            AppDestination.AI_SPEAKING_PRACTICE -> navigateTo(AppDestination.MAIN_LEARN)
            AppDestination.MOCK_TEST_RUNNER -> navigateTo(AppDestination.MAIN_TESTS)
            AppDestination.GRAMMAR_LAB -> navigateTo(AppDestination.MAIN_LEARN)
            AppDestination.RESULT_ANALYSIS,
            AppDestination.TEST_HISTORY -> navigateTo(AppDestination.MAIN_HOME)
            AppDestination.QUESTION_REVIEW -> navigateTo(AppDestination.TEST_HISTORY)
            AppDestination.TEACHER_DASHBOARD,
            AppDestination.ADMIN_DASHBOARD,
            AppDestination.ABOUT -> navigateTo(AppDestination.MAIN_PROFILE)
            else -> navigateTo(AppDestination.MAIN_HOME)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = BackgroundDark,
        bottomBar = {
            if (isBottomBarVisible) {
                FuturisticBottomNavigationBar(
                    currentDestination = currentDestination,
                    onNavigate = { dest -> currentDestination = dest }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (isBottomBarVisible) 76.dp else 0.dp)
        ) {
            when (currentDestination) {
                AppDestination.SPLASH -> {
                    SplashScreen(
                        onNavigateNext = { navigateTo(AppDestination.ONBOARDING) }
                    )
                }

                AppDestination.ONBOARDING -> {
                    OnboardingScreen(
                        onComplete = { navigateTo(AppDestination.MAIN_HOME) }
                    )
                }

                AppDestination.AUTH -> {
                    AuthScreen(
                        onLoginSuccess = { navigateTo(AppDestination.MAIN_HOME) }
                    )
                }

                AppDestination.MAIN_HOME -> {
                    HomeScreen(
                        onNavigateToModule = { module ->
                            when (module) {
                                IeltsModuleType.LISTENING -> navigateTo(AppDestination.LISTENING_PRACTICE)
                                IeltsModuleType.READING -> navigateTo(AppDestination.READING_PRACTICE)
                                IeltsModuleType.WRITING -> navigateTo(AppDestination.WRITING_PRACTICE)
                                IeltsModuleType.SPEAKING -> navigateTo(AppDestination.SPEAKING_PRACTICE)
                                IeltsModuleType.VOCABULARY -> navigateTo(AppDestination.MAIN_VOCAB)
                                IeltsModuleType.GRAMMAR -> navigateTo(AppDestination.GRAMMAR_LAB)
                            }
                        },
                        onNavigateToMockTest = { navigateTo(AppDestination.MAIN_TESTS) },
                        onNavigateToResult = {
                            resultScoreOverride = 7.0
                            navigateTo(AppDestination.RESULT_ANALYSIS)
                        },
                        onRoleChanged = { /* state handled in repository */ },
                        onNavigateToHistory = { navigateTo(AppDestination.TEST_HISTORY) }
                    )
                }

                AppDestination.MAIN_LEARN -> {
                    LearnScreen(
                        onNavigateToModule = { module ->
                            when (module) {
                                IeltsModuleType.LISTENING -> navigateTo(AppDestination.LISTENING_PRACTICE)
                                IeltsModuleType.READING -> navigateTo(AppDestination.READING_PRACTICE)
                                IeltsModuleType.WRITING -> navigateTo(AppDestination.WRITING_PRACTICE)
                                IeltsModuleType.SPEAKING -> navigateTo(AppDestination.SPEAKING_PRACTICE)
                                IeltsModuleType.VOCABULARY -> navigateTo(AppDestination.MAIN_VOCAB)
                                IeltsModuleType.GRAMMAR -> navigateTo(AppDestination.GRAMMAR_LAB)
                            }
                        },
                        onNavigateToVocab = { navigateTo(AppDestination.MAIN_VOCAB) },
                        onNavigateToGrammar = { navigateTo(AppDestination.GRAMMAR_LAB) },
                        onNavigateToAiSpeaking = { navigateTo(AppDestination.AI_SPEAKING_PRACTICE) }
                    )
                }

                AppDestination.MAIN_TESTS -> {
                    MockTestCenterScreen(
                        onStartTest = { testId ->
                            activeRunningTestId = testId
                            navigateTo(AppDestination.MOCK_TEST_RUNNER)
                        }
                    )
                }

                AppDestination.MOCK_TEST_RUNNER -> {
                    MockTestRunnerScreen(
                        testId = activeRunningTestId,
                        onNavigateBack = { navigateTo(AppDestination.MAIN_TESTS) },
                        onTestSubmitted = { band ->
                            resultScoreOverride = band
                            navigateTo(AppDestination.RESULT_ANALYSIS)
                        }
                    )
                }

                AppDestination.MAIN_VOCAB -> {
                    VocabLabScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) }
                    )
                }

                AppDestination.MAIN_PROFILE -> {
                    ProfileScreen(
                        onSignOut = { navigateTo(AppDestination.AUTH) },
                        onOpenTeacherDashboard = { navigateTo(AppDestination.TEACHER_DASHBOARD) },
                        onOpenAdminDashboard = { navigateTo(AppDestination.ADMIN_DASHBOARD) },
                        onOpenAbout = { navigateTo(AppDestination.ABOUT) }
                    )
                }

                AppDestination.LISTENING_PRACTICE -> {
                    ListeningPracticeScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) },
                        onNavigateToResult = { band ->
                            resultScoreOverride = band
                            navigateTo(AppDestination.RESULT_ANALYSIS)
                        }
                    )
                }

                AppDestination.READING_PRACTICE -> {
                    ReadingPracticeScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) },
                        onNavigateToResult = { band ->
                            resultScoreOverride = band
                            navigateTo(AppDestination.RESULT_ANALYSIS)
                        }
                    )
                }

                AppDestination.WRITING_PRACTICE -> {
                    WritingPracticeScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) },
                        onNavigateToTeacherEvaluation = { navigateTo(AppDestination.TEACHER_DASHBOARD) }
                    )
                }

                AppDestination.SPEAKING_PRACTICE -> {
                    SpeakingPracticeScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) }
                    )
                }

                AppDestination.AI_SPEAKING_PRACTICE -> {
                    AiSpeakingPracticeScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_LEARN) }
                    )
                }

                AppDestination.RESULT_ANALYSIS -> {
                    ResultScreen(
                        customBand = resultScoreOverride,
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) },
                        onStartRecommendedPractice = { module ->
                            navigateTo(AppDestination.WRITING_PRACTICE)
                        },
                        onNavigateToReview = {
                            navigateTo(AppDestination.QUESTION_REVIEW)
                        }
                    )
                }

                AppDestination.TEST_HISTORY -> {
                    TestHistoryScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_HOME) },
                        onOpenAttemptReview = { attemptId ->
                            activeReviewAttemptId = attemptId
                            navigateTo(AppDestination.QUESTION_REVIEW)
                        },
                        onStartNewTest = { navigateTo(AppDestination.MAIN_TESTS) }
                    )
                }

                AppDestination.QUESTION_REVIEW -> {
                    QuestionReviewScreen(
                        attemptId = activeReviewAttemptId,
                        onNavigateBack = { navigateTo(AppDestination.TEST_HISTORY) }
                    )
                }

                AppDestination.GRAMMAR_LAB -> {
                    GrammarLabScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_LEARN) }
                    )
                }

                AppDestination.TEACHER_DASHBOARD -> {
                    TeacherDashboardScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_PROFILE) }
                    )
                }

                AppDestination.ADMIN_DASHBOARD -> {
                    AdminDashboardScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_PROFILE) }
                    )
                }

                AppDestination.ABOUT -> {
                    AboutScreen(
                        onNavigateBack = { navigateTo(AppDestination.MAIN_PROFILE) },
                        onSignOut = { navigateTo(AppDestination.AUTH) }
                    )
                }

                else -> {
                    HomeScreen(
                        onNavigateToModule = {},
                        onNavigateToMockTest = {},
                        onNavigateToResult = {},
                        onRoleChanged = {}
                    )
                }
            }
        }
    }
}

@Composable
fun FuturisticBottomNavigationBar(
    currentDestination: AppDestination,
    onNavigate: (AppDestination) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = Color(0xEB0D1424),
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                label = "Home",
                selected = currentDestination == AppDestination.MAIN_HOME,
                iconSelected = Icons.Filled.Home,
                iconUnselected = Icons.Outlined.Home,
                testTag = "nav_home",
                onClick = { onNavigate(AppDestination.MAIN_HOME) }
            )

            BottomNavItem(
                label = "Learn",
                selected = currentDestination == AppDestination.MAIN_LEARN,
                iconSelected = Icons.Filled.School,
                iconUnselected = Icons.Outlined.School,
                testTag = "nav_learn",
                onClick = { onNavigate(AppDestination.MAIN_LEARN) }
            )

            BottomNavItem(
                label = "Tests",
                selected = currentDestination == AppDestination.MAIN_TESTS,
                iconSelected = Icons.Filled.Assignment,
                iconUnselected = Icons.Outlined.Assignment,
                testTag = "nav_tests",
                onClick = { onNavigate(AppDestination.MAIN_TESTS) }
            )

            BottomNavItem(
                label = "Vocab",
                selected = currentDestination == AppDestination.MAIN_VOCAB,
                iconSelected = Icons.Filled.Style,
                iconUnselected = Icons.Outlined.Style,
                testTag = "nav_vocab",
                onClick = { onNavigate(AppDestination.MAIN_VOCAB) }
            )

            BottomNavItem(
                label = "Profile",
                selected = currentDestination == AppDestination.MAIN_PROFILE,
                iconSelected = Icons.Filled.Person,
                iconUnselected = Icons.Outlined.Person,
                testTag = "nav_profile",
                onClick = { onNavigate(AppDestination.MAIN_PROFILE) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    label: String,
    selected: Boolean,
    iconSelected: ImageVector,
    iconUnselected: ImageVector,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 28.dp)
                .clip(CircleShape)
                .background(if (selected) NeonCyan.copy(alpha = 0.2f) else Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (selected) iconSelected else iconUnselected,
                contentDescription = label,
                tint = if (selected) NeonCyan else TextSecondary,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) NeonCyan else TextSecondary
            )
        )
    }
}
