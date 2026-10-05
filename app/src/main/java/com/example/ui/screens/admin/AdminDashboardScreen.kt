package com.example.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun AdminDashboardScreen(
    onNavigateBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("admin_dashboard_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("admin_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "ACADEMY COMMAND CONSOLE",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ADMINISTRATOR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Academy Live Telemetry
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.45f)
            ) {
                Text(
                    text = "ACADEMY LIVE ENROLLMENT & SYSTEM METRICS",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AdminMetricStat("Active Students", "1,248", NeonCyan)
                    AdminMetricStat("Mock Tests Taken", "3,892", ElectricViolet)
                    AdminMetricStat("Avg Score Gain", "+1.2 Band", NeonGreen)
                }
            }
        }

        // Database Architecture (Supabase PostgreSQL Status)
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricViolet.copy(alpha = 0.4f)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Storage, contentDescription = null, tint = ElectricViolet)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SUPABASE POSTGRESQL ARCHITECTURE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                listOf(
                    "profiles & student_profiles" to "Active RLS (Row Level Security)",
                    "writing_submissions & evaluations" to "Teacher-assigned access policy",
                    "test_sets & score_conversion_tables" to "Configurable academy schema",
                    "Supabase Storage Buckets" to "Encrypted private audio recordings"
                ).forEach { (table, status) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(table, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary))
                        Text(status, style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen))
                    }
                }
            }
        }

        // Content Licensing & Compliance
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.4f)
            ) {
                Text(
                    text = "CONTENT LICENSING & ATTRIBUTION",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonAmber,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Lead Architect: Sahil VANTIS\nAcademy: DAWOOD BHAI IELTS STUDIO\nLicensing: Authorized Proprietary & Open Educational Curriculum",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, lineHeight = 20.sp)
                )
            }
        }

        // Real Database Content Management Action
        item {
            var showTestCreatedNotification by remember { mutableStateOf(false) }

            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonGreen.copy(alpha = 0.4f),
                testTag = "admin_deploy_test_card"
            ) {
                Text(
                    text = "DYNAMIC ACADEMY CONTENT DEPLOYMENT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonGreen,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Instantly inject new curriculum mock tests into PostgreSQL / Supabase repository without re-compiling the APK.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(12.dp))

                FuturisticGlowButton(
                    text = "DEPLOY NEW GENERAL TRAINING TEST #2",
                    onClick = {
                        val repo = com.example.data.repository.IeltsRepository.getInstance()
                        repo.addMockTest(
                            com.example.data.model.MockTest(
                                id = "test_gt_02_${System.currentTimeMillis()}",
                                title = "General Training Workplace Mock #2",
                                ieltsType = com.example.data.model.IeltsType.GENERAL_TRAINING,
                                difficulty = "Standard Exam Standard",
                                durationMinutes = 150,
                                totalQuestions = 40,
                                moduleType = com.example.data.model.IeltsModuleType.READING,
                                sections = listOf(
                                    com.example.data.model.TestSection(
                                        sectionNumber = 1,
                                        title = "Section 1: Occupational Health & Workplace Safety Guidelines",
                                        instructions = "Answer the following question based on workplace safety protocols.",
                                        questions = listOf(
                                            com.example.data.model.TestQuestion(
                                                id = "qgt_1",
                                                questionNumber = 1,
                                                questionType = com.example.data.model.QuestionType.MULTIPLE_CHOICE,
                                                prompt = "Mandatory reporting window for workplace hazard incidents:",
                                                options = listOf(
                                                    com.example.data.model.QuestionOption("a", "A", "Within 24 hours"),
                                                    com.example.data.model.QuestionOption("b", "B", "Within 48 hours"),
                                                    com.example.data.model.QuestionOption("c", "C", "Within 7 business days")
                                                ),
                                                correctAnswer = "Within 24 hours",
                                                explanation = "Statutory regulations stipulate critical hazard reporting within 24 hours."
                                            )
                                        )
                                    )
                                ),
                                contentSource = "Dawood Bhai IELTS Studio Dynamic Engine",
                                copyrightOwner = "Dawood Bhai IELTS Studio & Sahil VANTIS",
                                licenseStatus = "Production Academy Deployment",
                                isCompleted = false
                            )
                        )
                        showTestCreatedNotification = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonCyan,
                    contentColor = BackgroundDark,
                    testTag = "admin_publish_test_btn"
                )

                if (showTestCreatedNotification) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "✓ Live test successfully deployed to candidate Mock Test Center!",
                        style = MaterialTheme.typography.bodySmall.copy(color = NeonGreen, fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
private fun AdminMetricStat(title: String, value: String, color: Color) {
    Column {
        Text(title, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = color
            )
        )
    }
}
