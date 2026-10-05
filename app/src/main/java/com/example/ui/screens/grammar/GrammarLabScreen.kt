package com.example.ui.screens.grammar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.data.model.GrammarTopic
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun GrammarLabScreen(
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val grammarTopics by repository.grammarTopics.collectAsState()

    var selectedTopicId by remember { mutableStateOf(grammarTopics.firstOrNull()?.id ?: "") }
    val activeTopic = grammarTopics.find { it.id == selectedTopicId } ?: grammarTopics.first()

    val quizAnswers = remember { mutableStateMapOf<String, Int>() }
    var isQuizSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("grammar_lab_screen"),
        contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Bar
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.testTag("grammar_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "IELTS GRAMMAR LAB",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = ElectricViolet,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // Horizontal Topic Selector Pills
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                grammarTopics.forEach { topic ->
                    val isSelected = selectedTopicId == topic.id
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) ElectricViolet else SurfaceCard)
                            .clickable {
                                selectedTopicId = topic.id
                                isQuizSubmitted = false
                            }
                            .padding(horizontal = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = topic.title.split(" ").take(2).joinToString(" "),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Lesson Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricViolet.copy(alpha = 0.5f),
                testTag = "grammar_lesson_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = activeTopic.tag,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                    Text(
                        text = activeTopic.bandImpact,
                        style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = activeTopic.title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = activeTopic.explanation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextPrimary,
                        lineHeight = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "ACADEMIC RULES",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = ElectricViolet,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                activeTopic.keyRules.forEach { rule ->
                    Text(
                        text = "• $rule",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }

        // Exemplar Comparative Contrast (Informal vs Academic Band 8+)
        item {
            Text(
                text = "CORPUS EXAMPLES & ANALYSIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                activeTopic.examples.forEach { (type, sentence) ->
                    FuturisticGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = if (type.contains("Academic") || type.contains("Advanced")) NeonCyan.copy(alpha = 0.4f) else SurfaceBorder
                    ) {
                        Text(
                            text = type.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (type.contains("Academic") || type.contains("Advanced")) NeonCyan else TextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = sentence,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }

        // Interactive Grammar Quiz
        item {
            Text(
                text = "PRACTICE DRILL QUIZ",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            activeTopic.quiz.forEach { q ->
                val selectedIdx = quizAnswers[q.id]
                val isCorrect = selectedIdx == q.correctIndex

                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (isQuizSubmitted) {
                        if (isCorrect) NeonGreen else NeonRed
                    } else SurfaceBorder,
                    testTag = "quiz_card_${q.id}"
                ) {
                    Text(
                        text = q.prompt,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    q.options.forEachIndexed { idx, opt ->
                        val isOptionSelected = selectedIdx == idx
                        val isThisCorrectOpt = idx == q.correctIndex

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (isQuizSubmitted && isThisCorrectOpt) NeonGreen.copy(alpha = 0.15f)
                                    else if (isOptionSelected) ElectricViolet.copy(alpha = 0.15f)
                                    else SurfaceElevated
                                )
                                .clickable {
                                    if (!isQuizSubmitted) quizAnswers[q.id] = idx
                                }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = isOptionSelected,
                                onClick = { if (!isQuizSubmitted) quizAnswers[q.id] = idx },
                                colors = RadioButtonDefaults.colors(selectedColor = ElectricViolet)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = opt,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isQuizSubmitted && isThisCorrectOpt) NeonGreen else TextPrimary
                                )
                            )
                        }
                    }

                    if (isQuizSubmitted) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = q.explanation,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isQuizSubmitted) {
                FuturisticGlowButton(
                    text = "CHECK DRILL ANSWERS",
                    onClick = { isQuizSubmitted = true },
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = ElectricViolet,
                    testTag = "submit_quiz_btn"
                )
            }
        }
    }
}
