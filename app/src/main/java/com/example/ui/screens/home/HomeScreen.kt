package com.example.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.IeltsModuleType
import com.example.data.model.UserRole
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.components.FuturisticScoreRing
import com.example.ui.theme.*

@Composable
fun HomeScreen(
    onNavigateToModule: (IeltsModuleType) -> Unit,
    onNavigateToMockTest: () -> Unit,
    onNavigateToResult: () -> Unit,
    onRoleChanged: () -> Unit,
    onNavigateToHistory: () -> Unit = {}
) {
    val repository = remember { IeltsRepository.getInstance() }
    val userProfile by repository.userProfile.collectAsState()
    val dailyMissions by repository.dailyMissions.collectAsState()
    val completedAttempts by repository.completedAttempts.collectAsState()

    val completedMissions = dailyMissions.count { it.isCompleted }
    val totalMissions = dailyMissions.size
    val missionProgress = if (totalMissions > 0) completedMissions.toFloat() / totalMissions else 0f

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 18.dp)
            .testTag("home_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Top Header with Greeting & Role Badge
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (userProfile.name.isNotBlank()) "Good Evening, ${userProfile.name}" else "Welcome, Candidate",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        modifier = Modifier.testTag("greeting_text")
                    )
                    Text(
                        text = "Ready to reach Band ${String.format("%.1f", userProfile.targetBand)}?",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                // Role Switcher Chip
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceElevated)
                        .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .clickable {
                            val nextRole = when (userProfile.role) {
                                UserRole.STUDENT -> UserRole.TEACHER
                                UserRole.TEACHER -> UserRole.ADMIN
                                UserRole.ADMIN -> UserRole.STUDENT
                            }
                            repository.switchUserRole(nextRole)
                            onRoleChanged()
                        }
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .testTag("home_role_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (userProfile.role == UserRole.STUDENT) NeonCyan else ElectricViolet)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = userProfile.role.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Hero Command Center Card with Animated Score Ring & Hero Graphic
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.45f),
                testTag = "hero_command_center_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GpsFixed,
                                contentDescription = null,
                                tint = NeonCyan,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "IELTS COMMAND STATUS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = "CURRENT BAND",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = String.format("%.1f", userProfile.currentBand),
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.Black
                                    )
                                )
                            }
                            Column {
                                Text(
                                    text = "TARGET",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = String.format("%.1f", userProfile.targetBand),
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                            Column {
                                Text(
                                    text = "DAYS LEFT",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "${userProfile.examDateDaysLeft}",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        color = ElectricViolet,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            // Streak chip
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🔥", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userProfile.streakDays} Day Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(color = NeonAmber)
                                )
                            }

                            // Study time chip
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("⏱️", fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userProfile.studyHoursTotal}h Logged",
                                    style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen)
                                )
                            }
                        }
                    }

                    // Circular Glowing Arc
                    FuturisticScoreRing(
                        currentScore = userProfile.currentBand,
                        targetScore = userProfile.targetBand,
                        ringSize = 120.dp,
                        strokeWidth = 10.dp
                    )
                }
            }
        }

        // Today's Mission Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricViolet.copy(alpha = 0.35f),
                testTag = "todays_mission_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "TODAY'S IELTS MISSION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricViolet,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "$completedMissions / $totalMissions COMPLETED",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "+150 XP",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Progress Bar
                LinearProgressIndicator(
                    progress = { missionProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = NeonCyan,
                    trackColor = Color(0xFF1E293B)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Mission Items Checklist
                dailyMissions.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { repository.toggleMission(item.id) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.isCompleted,
                            onCheckedChange = { repository.toggleMission(item.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = NeonCyan,
                                uncheckedColor = Color(0xFF64748B),
                                checkmarkColor = BackgroundDark
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (item.isCompleted) TextMuted else TextPrimary,
                                fontWeight = if (item.isCompleted) FontWeight.Normal else FontWeight.Medium
                            ),
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "${item.durationMinutes} min",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                FuturisticGlowButton(
                    text = "CONTINUE MISSION",
                    onClick = { onNavigateToModule(IeltsModuleType.WRITING) },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "continue_mission_btn"
                )
            }
        }

        // Four IELTS Module Cards
        item {
            Text(
                text = "CORE IELTS MODULES",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Listening Card
                ModuleScoreCard(
                    title = "LISTENING",
                    score = userProfile.listeningBand,
                    subtitle = "+0.5 this week",
                    accentColor = NeonCyan,
                    testTag = "module_card_listening",
                    onContinue = { onNavigateToModule(IeltsModuleType.LISTENING) }
                )

                // Reading Card
                ModuleScoreCard(
                    title = "READING",
                    score = userProfile.readingBand,
                    subtitle = "Consistent performance",
                    accentColor = ElectricViolet,
                    testTag = "module_card_reading",
                    onContinue = { onNavigateToModule(IeltsModuleType.READING) }
                )

                // Writing Card
                ModuleScoreCard(
                    title = "WRITING",
                    score = userProfile.writingBand,
                    subtitle = "Needs improvement",
                    accentColor = NeonAmber,
                    testTag = "module_card_writing",
                    onContinue = { onNavigateToModule(IeltsModuleType.WRITING) }
                )

                // Speaking Card
                ModuleScoreCard(
                    title = "SPEAKING",
                    score = userProfile.speakingBand,
                    subtitle = "Practice cue cards",
                    accentColor = NeonGreen,
                    testTag = "module_card_speaking",
                    onContinue = { onNavigateToModule(IeltsModuleType.SPEAKING) }
                )
            }
        }

        // Intelligent Recommendation Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.4f),
                testTag = "recommendation_card"
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(NeonAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "INTELLIGENT RECOMMENDATION",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.8.sp
                            )
                        )
                        Text(
                            text = "Your weakest skill is Writing Task 2",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Diagnostic insight: Focusing on Coherence & Cohesion transitions will elevate your essay band from 6.5 to 7.5.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Estimated time: 25 minutes",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                    )
                    FuturisticGlowButton(
                        text = "START PRACTICE",
                        onClick = { onNavigateToModule(IeltsModuleType.WRITING) },
                        containerColor = NeonAmber,
                        contentColor = BackgroundDark,
                        testTag = "start_recommended_practice_btn"
                    )
                }
            }
        }

        // Upcoming Mock Test Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.3f),
                testTag = "upcoming_mock_test_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "ACADEMY MOCK TEST CENTER",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Academic Full Mock Test #1",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Listening, Reading, Writing • 40 Questions",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }

                    FuturisticGlowButton(
                        text = "TAKE TEST",
                        onClick = onNavigateToMockTest,
                        testTag = "take_mock_test_btn"
                    )
                }
            }
        }

        // Recent Test Attempts & History Section (Database Driven)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECENT TEST ATTEMPTS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    if (completedAttempts.isNotEmpty()) {
                        Text(
                            text = "View All History",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier
                                .clickable { onNavigateToHistory() }
                                .testTag("view_all_history_btn")
                        )
                    }
                }

                if (completedAttempts.isEmpty()) {
                    FuturisticGlassCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToMockTest() },
                        borderColor = SurfaceBorder,
                        testTag = "home_empty_attempts_card"
                    ) {
                        Text(
                            text = "No tests completed yet.",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Complete your first timed practice or mock test to establish your personalized band history and diagnostic review.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Tap to launch Mock Test Center →",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                        )
                    }
                } else {
                    completedAttempts.take(2).forEach { att ->
                        FuturisticGlassCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToHistory() },
                            borderColor = NeonCyan.copy(alpha = 0.3f),
                            testTag = "home_attempt_item_${att.id}"
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = att.testTitle,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${att.rawScore}/${att.totalQuestions} Correct • ${att.timeTakenSeconds / 60}m spent",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                    )
                                }
                                Text(
                                    text = String.format("Band %.1f", att.bandScore),
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = NeonGreen
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // Recent Results Link
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(SurfaceCard)
                    .border(1.dp, SurfaceBorder, RoundedCornerShape(14.dp))
                    .clickable { onNavigateToResult() }
                    .padding(16.dp)
                    .testTag("view_recent_results_card")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = null,
                            tint = NeonCyan
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "View Recent Mock Test Analysis",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Estimated Overall Band: 7.0 • Target Gap: 0.5",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Navigate to results",
                        tint = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun ModuleScoreCard(
    title: String,
    score: Double,
    subtitle: String,
    accentColor: Color,
    testTag: String,
    onContinue: () -> Unit
) {
    FuturisticGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onContinue() },
        borderColor = accentColor.copy(alpha = 0.3f),
        testTag = testTag
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Text(
                    text = String.format("%.1f", score),
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = TextPrimary
                    )
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(SurfaceElevated)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Continue",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
