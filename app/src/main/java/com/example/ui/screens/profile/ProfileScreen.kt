package com.example.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IeltsType
import com.example.data.model.UserRole
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun ProfileScreen(
    onSignOut: () -> Unit,
    onOpenTeacherDashboard: () -> Unit,
    onOpenAdminDashboard: () -> Unit,
    onOpenAbout: () -> Unit = {}
) {
    val repository = remember { IeltsRepository.getInstance() }
    val userProfile by repository.userProfile.collectAsState()

    var showEditTargetDialog by remember { mutableStateOf(false) }
    var tempTargetBand by remember { mutableDoubleStateOf(userProfile.targetBand) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("profile_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Text(
                text = "CANDIDATE COMMAND PROFILE",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            )
            Text(
                text = "Account & Academy Status",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
        }

        // Profile Identity Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.45f),
                testTag = "profile_identity_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userProfile.name.take(2).uppercase(),
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonCyan
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userProfile.name,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = userProfile.email,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SurfaceElevated)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${userProfile.role.name} • ${userProfile.ieltsType.displayName}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ProfileStat("Current Band", String.format("%.1f", userProfile.currentBand), TextPrimary)
                    ProfileStat("Target Band", String.format("%.1f", userProfile.targetBand), NeonCyan)
                    ProfileStat("Days Left", "${userProfile.examDateDaysLeft}", ElectricViolet)
                    ProfileStat("XP Points", "${userProfile.xp}", NeonGreen)
                }
            }
        }

        // Target Band Settings Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SurfaceBorder
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Target Band Calibration",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Currently aiming for Band ${String.format("%.1f", userProfile.targetBand)}",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                    Button(
                        onClick = { showEditTargetDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = SurfaceElevated)
                    ) {
                        Text("Edit", color = NeonCyan)
                    }
                }
            }
        }

        // Gamification Achievements
        item {
            Text(
                text = "ACADEMY ACHIEVEMENTS & BADGES",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AchievementRow("🔥 7-Day Consistency Streak", "Completed consecutive daily IELTS missions", true)
                AchievementRow("🎯 First Mock Test Completed", "Scored Band 7.5 in Academic Mock Test 1", true)
                AchievementRow("📚 100 Academic Words Mastered", "Completed flashcards in Vocabulary Lab", true)
                AchievementRow("✍️ Writing Task 2 Submitted", "Dispatched essay for teacher evaluation", true)
            }
        }

        // Role Switch Shortcuts
        item {
            Text(
                text = "ROLE SWITCH SHORTCUTS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenTeacherDashboard,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Teacher Hub", fontSize = 12.sp)
                }

                OutlinedButton(
                    onClick = onOpenAdminDashboard,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.AdminPanelSettings, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Admin Console", fontSize = 12.sp)
                }
            }
        }

        // Developer Credit Card (Strictly Sahil VANTIS as requested)
        item {
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAbout() },
                borderColor = ElectricViolet.copy(alpha = 0.5f),
                testTag = "developer_credit_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(ElectricViolet.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "DAWOOD BHAI IELTS STUDIO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Designed & Developed by Sahil VANTIS",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Architectural Engineering & Futuristic UI • v1.0.0",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        // Sign Out Button
        item {
            FuturisticGlowButton(
                text = "SIGN OUT / SWITCH USER",
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth(),
                containerColor = SurfaceElevated,
                contentColor = NeonRed,
                testTag = "sign_out_btn"
            )
        }
    }

    if (showEditTargetDialog) {
        AlertDialog(
            onDismissRequest = { showEditTargetDialog = false },
            title = { Text("Target Band Score", color = TextPrimary) },
            text = {
                Column {
                    Text("Select your desired IELTS Band target:", color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Band ${String.format("%.1f", tempTargetBand)}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonCyan
                            )
                        )
                    }
                    Slider(
                        value = tempTargetBand.toFloat(),
                        onValueChange = {
                            val rounded = (Math.round(it * 2) / 2.0).coerceIn(6.0, 9.0)
                            tempTargetBand = rounded
                        },
                        valueRange = 6f..9f,
                        steps = 5,
                        colors = SliderDefaults.colors(thumbColor = NeonCyan, activeTrackColor = NeonCyan)
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        repository.updateProfile(tempTargetBand, userProfile.ieltsType, userProfile.dailyGoalMinutes)
                        showEditTargetDialog = false
                    }
                ) {
                    Text("Save", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditTargetDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}

@Composable
private fun ProfileStat(label: String, value: String, valueColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = valueColor
            )
        )
    }
}

@Composable
private fun AchievementRow(title: String, desc: String, isUnlocked: Boolean) {
    FuturisticGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (isUnlocked) NeonGreen.copy(alpha = 0.35f) else SurfaceBorder
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = NeonGreen,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
