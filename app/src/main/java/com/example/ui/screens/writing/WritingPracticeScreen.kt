package com.example.ui.screens.writing

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun WritingPracticeScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTeacherEvaluation: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val prompt = repository.currentWritingPrompt

    var essayText by remember {
        mutableStateOf(
            "In contemporary society, the integration of artificial intelligence into pedagogical frameworks has ignited contentious discourse. While proponents postulate that adaptive algorithms foster personalized learning trajectories, critics assert that overreliance on automated systems could atrophy critical cognitive capabilities. In my opinion, AI serves as an indispensable catalyst for academic innovation, provided rigorous academic integrity paradigms are maintained.\n\nOn the one hand, neural synthesis engines furnish bespoke didactic material tailored to individual comprehension rates. For instance, struggling learners receive granular scaffolding, thereby bridging the achievement divide without encumbering classroom instructors. Furthermore, automated evaluative mechanisms afford instantaneous diagnostic feedback, expediting revision cycles significantly.\n\nConversely, unfettered reliance poses palpable perils to scholarly rigor. If undergraduates delegate heuristic inquiry to generative models, analytical synthesis will inevitably degenerate into superficial regurgitation. Therefore, institutional stakeholders must implement comprehensive ethical guidelines rather than outright proscriptions.\n\nIn conclusion, rather than thwarting technological disruption, universities ought to harness artificial intelligence to augment intellectual curiosity while safeguarding the paramount sanctity of original scholarly analysis."
        )
    }

    // Word counter
    val wordCount = remember(essayText) {
        if (essayText.isBlank()) 0 else essayText.trim().split("\\s+".toRegex()).size
    }

    var isSubmitted by remember { mutableStateOf(false) }
    var showSuccessSnackbar by remember { mutableStateOf(false) }
    var aiDiagnosticResult by remember { mutableStateOf<Map<String, Double>?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("writing_practice_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Navigation & Word Count Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("writing_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "IELTS WRITING STUDIO",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonAmber,
                        letterSpacing = 1.sp
                    )
                )

                // Timer Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(SurfaceElevated)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "34:12 Left",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonAmber,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Prompt Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonAmber.copy(alpha = 0.4f),
                testTag = "writing_prompt_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = prompt.taskType,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonAmber,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Suggested: ${prompt.suggestedTimeMinutes} mins",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = prompt.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = prompt.prompt,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = prompt.instructions,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
            }
        }

        // Live Word Count Status Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Words: $wordCount",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (wordCount >= prompt.minWords) NeonGreen else NeonAmber
                        ),
                        modifier = Modifier.testTag("writing_word_count_text")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "(Min ${prompt.minWords} words required)",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }

                if (wordCount < prompt.minWords) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonAmber.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Underlength Penalty Risk",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(NeonGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Length Requirement Met ✓",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // Text Editor Area
        item {
            OutlinedTextField(
                value = essayText,
                onValueChange = { essayText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 280.dp)
                    .testTag("writing_editor_input"),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedContainerColor = SurfaceCard,
                    unfocusedContainerColor = SurfaceCard,
                    focusedBorderColor = NeonAmber,
                    unfocusedBorderColor = SurfaceBorder
                ),
                placeholder = {
                    Text(
                        text = "Compose your academic response here...",
                        color = TextMuted
                    )
                }
            )
        }

        // IELTS 4 Evaluation Criteria Guide
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SurfaceBorder
            ) {
                Text(
                    text = "IELTS ASSESSMENT CRITERIA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CriteriaPill("Task Response (25%)")
                    CriteriaPill("Coherence (25%)")
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    CriteriaPill("Lexical Resource (25%)")
                    CriteriaPill("Grammar (25%)")
                }
            }
        }

        // Actions: Save Draft, AI Check, & Submit
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            aiDiagnosticResult = repository.evaluateWritingAiAssisted(essayText, prompt.minWords)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("ai_check_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan)
                    ) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("AI Rubric Check", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = { showSuccessSnackbar = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("save_draft_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
                        border = ButtonDefaults.outlinedButtonBorder(true)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Save Draft", fontSize = 12.sp)
                    }
                }

                FuturisticGlowButton(
                    text = if (isSubmitted) "SUBMITTED TO TEACHER ✓" else "DISPATCH ESSAY TO TEACHER",
                    onClick = {
                        repository.submitWritingEssay(essayText, wordCount)
                        isSubmitted = true
                        showSuccessSnackbar = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = NeonAmber,
                    testTag = "submit_essay_btn"
                )
            }
        }

        // AI Diagnostic Result Display
        if (aiDiagnosticResult != null) {
            val diag = aiDiagnosticResult!!
            item {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.5f),
                    testTag = "ai_diagnostic_card"
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "AI DIAGNOSTIC RUBRIC ESTIMATE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonCyan,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "Band ${String.format("%.1f", diag["OVERALL"] ?: 7.0)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonGreen
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Task Response: ${diag["TR"]}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            Text("Coherence: ${diag["CC"]}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                        Column {
                            Text("Lexical Resource: ${diag["LR"]}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                            Text("Grammar: ${diag["GRA"]}", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                        }
                    }
                }
            }
        }

        if (showSuccessSnackbar) {
            item {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonGreen
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Essay Dispatched to Academy Queue",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreen
                                )
                            )
                            Text(
                                text = "Awaiting teacher band rubric evaluation",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                        TextButton(onClick = onNavigateToTeacherEvaluation) {
                            Text("Open Teacher View", color = NeonCyan, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CriteriaPill(label: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceElevated)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
        )
    }
}
