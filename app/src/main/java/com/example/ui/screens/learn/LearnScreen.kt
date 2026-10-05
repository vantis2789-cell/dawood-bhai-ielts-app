package com.example.ui.screens.learn

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.theme.*

@Composable
fun LearnScreen(
    onNavigateToModule: (IeltsModuleType) -> Unit,
    onNavigateToVocab: () -> Unit,
    onNavigateToGrammar: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 18.dp)
            .testTag("learn_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "IELTS LEARNING ACADEMY",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            )
            Text(
                text = "Mastery Modules & Labs",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Systematic lessons, authentic exam strategies, and diagnostic drills designed by Dawood Bhai IELTS Studio.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
        }

        // Section: Four Core Modules
        item {
            Text(
                text = "CORE TEST SKILLS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LearnModuleCard(
                    title = "IELTS Listening Mastery",
                    subtitle = "Sections 1–4 • Form completion, MCQs, map labeling with interactive audio player",
                    icon = Icons.Default.Headphones,
                    accentColor = NeonCyan,
                    badgeText = "4 Sections Available",
                    testTag = "learn_card_listening",
                    onClick = { onNavigateToModule(IeltsModuleType.LISTENING) }
                )

                LearnModuleCard(
                    title = "IELTS Reading Mastery",
                    subtitle = "Academic & General • True/False/Not Given, Matching Headings, timed passages",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    accentColor = ElectricViolet,
                    badgeText = "Split-Pane Mode",
                    testTag = "learn_card_reading",
                    onClick = { onNavigateToModule(IeltsModuleType.READING) }
                )

                LearnModuleCard(
                    title = "IELTS Writing Studio",
                    subtitle = "Task 1 (Report/Letter) & Task 2 (Essay) with live word counter & teacher evaluation",
                    icon = Icons.Default.EditNote,
                    accentColor = NeonAmber,
                    badgeText = "Band 9 Rubric",
                    testTag = "learn_card_writing",
                    onClick = { onNavigateToModule(IeltsModuleType.WRITING) }
                )

                LearnModuleCard(
                    title = "IELTS Speaking Lab",
                    subtitle = "Part 1, 2 (Cue Cards) & 3 • Audio recording with preparation & speaking timers",
                    icon = Icons.Default.Mic,
                    accentColor = NeonGreen,
                    badgeText = "Live Recorder",
                    testTag = "learn_card_speaking",
                    onClick = { onNavigateToModule(IeltsModuleType.SPEAKING) }
                )
            }
        }

        // Section: Specialized Labs
        item {
            Text(
                text = "SPECIALIZED LABS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                LearnModuleCard(
                    title = "Vocabulary Lab",
                    subtitle = "Band 7.5–9.0 academic flashcards, collocations, sample sentences, and mastery tracking",
                    icon = Icons.Default.Style,
                    accentColor = NeonCyan,
                    badgeText = "Flashcards & Quiz",
                    testTag = "learn_card_vocab",
                    onClick = onNavigateToVocab
                )

                LearnModuleCard(
                    title = "Grammar Lab",
                    subtitle = "Passive voice, inversion, advanced conditionals, and cohesive sentence construction",
                    icon = Icons.Default.AutoStories,
                    accentColor = ElectricViolet,
                    badgeText = "Band 8+ Structures",
                    testTag = "learn_card_grammar",
                    onClick = onNavigateToGrammar
                )
            }
        }
    }
}

@Composable
private fun LearnModuleCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    badgeText: String,
    testTag: String,
    onClick: () -> Unit
) {
    FuturisticGlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        borderColor = accentColor.copy(alpha = 0.35f),
        testTag = testTag
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = badgeText,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = accentColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Open module",
                tint = TextSecondary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
