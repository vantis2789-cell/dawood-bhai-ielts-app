package com.example.ui.screens.vocab

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VocabWord
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun VocabLabScreen(
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val vocabList by repository.vocabList.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedWordForCard by remember { mutableStateOf(vocabList.firstOrNull()) }
    var isCardFlipped by remember { mutableStateOf(false) }

    val filteredList = remember(searchQuery, vocabList) {
        if (searchQuery.isBlank()) vocabList
        else vocabList.filter {
            it.word.contains(searchQuery, ignoreCase = true) ||
                    it.definition.contains(searchQuery, ignoreCase = true) ||
                    it.topic.contains(searchQuery, ignoreCase = true)
        }
    }

    val rotation by animateFloatAsState(
        targetValue = if (isCardFlipped) 180f else 0f,
        animationSpec = tween(500),
        label = "card_flip"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("vocab_lab_screen"),
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
                    modifier = Modifier.testTag("vocab_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Text(
                    text = "IELTS VOCABULARY LAB",
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
                        text = "Band 8.0+",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Active Interactive Flashcard
        item {
            val word = selectedWordForCard ?: vocabList.first()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { isCardFlipped = !isCardFlipped }
                    .testTag("active_flashcard")
            ) {
                if (rotation <= 90f) {
                    // Front of card
                    FuturisticGlassCard(
                        modifier = Modifier.fillMaxSize(),
                        borderColor = NeonCyan.copy(alpha = 0.5f),
                        backgroundColor = SurfaceCard
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
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
                                        text = "BAND ${String.format("%.1f", word.ieltsBand)}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = NeonCyan,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Row {
                                    IconButton(onClick = { repository.toggleVocabFavorite(word.id) }) {
                                        Icon(
                                            imageVector = if (word.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                            contentDescription = "Favorite",
                                            tint = if (word.isFavorite) NeonRed else TextSecondary
                                        )
                                    }
                                }
                            }

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = word.word,
                                    style = MaterialTheme.typography.displayMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextPrimary
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "${word.phonetic} • ${word.partOfSpeech}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Topic: ${word.topic}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.TouchApp, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tap to Flip for Definition & Collocations", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted))
                            }
                        }
                    }
                } else {
                    // Back of card
                    FuturisticGlassCard(
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer { rotationY = 180f },
                        borderColor = ElectricViolet.copy(alpha = 0.5f),
                        backgroundColor = SurfaceElevated
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "DEFINITION",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = ElectricViolet,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = word.definition,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "IELTS SAMPLE SENTENCE",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonCyan,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = "\"${word.sampleSentence}\"",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        lineHeight = 18.sp
                                    )
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "ACADEMIC COLLOCATIONS",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = word.collocations.joinToString(" • "),
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = if (word.isMastered) "Mastered ✓" else "Not Mastered",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (word.isMastered) NeonGreen else TextSecondary
                                    )
                                )
                                Button(
                                    onClick = { repository.toggleVocabMastery(word.id) },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (word.isMastered) Color(0xFF1E293B) else NeonCyan
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = if (word.isMastered) "Unmark" else "Mark Mastered",
                                        color = if (word.isMastered) TextPrimary else BackgroundDark,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Search Input
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search academic vocabulary...", color = TextSecondary) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonCyan) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("vocab_search_input"),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = NeonCyan,
                    unfocusedBorderColor = SurfaceBorder
                ),
                singleLine = true
            )
        }

        // Vocabulary List
        item {
            Text(
                text = "HIGH-FREQUENCY LEXICAL REPOSITORY",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        items(filteredList) { item ->
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedWordForCard = item
                        isCardFlipped = false
                    },
                borderColor = if (selectedWordForCard?.id == item.id) NeonCyan else SurfaceBorder,
                testTag = "vocab_item_${item.id}"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.word,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = item.partOfSpeech,
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan)
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.definition,
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary),
                            maxLines = 2
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Band ${String.format("%.1f", item.ieltsBand)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = ElectricViolet,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}
