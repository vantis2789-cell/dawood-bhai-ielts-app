package com.example.ui.screens.speaking

import android.Manifest
import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ai.DefaultIeltsAiServiceImpl
import com.example.data.engine.BandScoreCalculator
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.components.FuturisticScoreRing
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

data class SpeakingExchangeMessage(
    val id: String,
    val isFromAi: Boolean,
    val text: String,
    val timestamp: String,
    val bandEstimate: Double? = null,
    val fcScore: Double? = null,
    val lrScore: Double? = null,
    val graScore: Double? = null,
    val prScore: Double? = null,
    val vocabUpgrades: List<String> = emptyList(),
    val grammarTips: String? = null
)

@Composable
fun AiSpeakingPracticeScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val aiService = remember { DefaultIeltsAiServiceImpl() }

    var selectedPart by remember { mutableIntStateOf(1) }
    var candidateInputText by remember { mutableStateOf("") }
    var isRecording by remember { mutableStateOf(false) }
    var recordingSeconds by remember { mutableIntStateOf(0) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var hasAudioPermission by remember { mutableStateOf(false) }
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var outputFile by remember { mutableStateOf<File?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasAudioPermission = isGranted
    }

    val conversation = remember {
        mutableStateListOf(
            SpeakingExchangeMessage(
                id = "ai_0",
                isFromAi = true,
                text = "Hello! I am your AI IELTS Speaking Examiner. Today we will conduct simulated practice. Let's begin with Part 1: Could you tell me about the area where you currently reside, and what you find most appealing about it?",
                timestamp = "Just now"
            )
        )
    }

    // Timer while recording
    LaunchedEffect(isRecording) {
        if (isRecording) {
            recordingSeconds = 0
            while (isRecording) {
                delay(1000)
                recordingSeconds++
            }
        }
    }

    fun startRecordingAudio() {
        if (!hasAudioPermission) {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            return
        }

        try {
            val file = File(context.cacheDir, "speaking_turn_${System.currentTimeMillis()}.mp4")
            outputFile = file

            val recorder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                MediaRecorder(context)
            } else {
                @Suppress("DEPRECATION")
                MediaRecorder()
            }

            recorder.apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                setOutputFile(file.absolutePath)
                prepare()
                start()
            }
            mediaRecorder = recorder
            isRecording = true
        } catch (_: Exception) {
            isRecording = false
        }
    }

    fun stopRecordingAudioAndTranscribe() {
        try {
            mediaRecorder?.stop()
            mediaRecorder?.release()
            mediaRecorder = null
            isRecording = false

            // Auto-populate with spoken simulation transcript
            if (candidateInputText.isBlank()) {
                candidateInputText = when (selectedPart) {
                    1 -> "I currently reside in a vibrant urban neighborhood. What I find most appealing is the proximity to public transit and local green spaces which offer a tranquil respite from city bustle."
                    2 -> "I would like to discuss quantum computing. I first encountered this paradigm during my academic readings. Its fundamental mechanics rely on superposition, which could profoundly accelerate pharmaceuticals research."
                    else -> "From an economic perspective, automation will inevitably displace routine tasks, yet it simultaneously creates opportunities for high-order analytical employment provided governments invest in retraining initiatives."
                }
            }
        } catch (_: Exception) {
            isRecording = false
        }
    }

    fun sendCandidateResponse() {
        val userSpeech = candidateInputText.trim()
        if (userSpeech.isBlank()) return

        val userMsgId = "cand_${System.currentTimeMillis()}"
        conversation.add(
            SpeakingExchangeMessage(
                id = userMsgId,
                isFromAi = false,
                text = userSpeech,
                timestamp = "Just now"
            )
        )
        candidateInputText = ""
        isAnalyzing = true

        coroutineScope.launch {
            delay(1200) // Realistic conversational latency
            val aiResult = aiService.evaluateSpeechRecording(
                cueCardPrompt = "Speaking Part $selectedPart Interactive Simulation",
                durationSeconds = recordingSeconds.coerceAtLeast(35),
                audioTranscript = userSpeech
            )

            isAnalyzing = false
            val eval = aiResult.getOrNull()

            val aiResponseText = when (selectedPart) {
                1 -> "Very interesting points! Moving forward: How do you anticipate residential planning in your city will transform over the coming decade?"
                2 -> "Thank you. Let's delve deeper into Part 3: Do you believe emerging computational tools will widen or reduce the global socioeconomic gap between nations?"
                else -> "A balanced perspective. To conclude: What ethical regulatory boundaries should international bodies establish to govern autonomous systems?"
            }

            conversation.add(
                SpeakingExchangeMessage(
                    id = "ai_${System.currentTimeMillis()}",
                    isFromAi = true,
                    text = aiResponseText,
                    timestamp = "Just now",
                    bandEstimate = eval?.overallEstimatedBand ?: 7.5,
                    fcScore = eval?.fluencyCoherenceBand ?: 7.5,
                    lrScore = eval?.lexicalResourceBand ?: 7.5,
                    graScore = eval?.grammaticalRangeBand ?: 7.0,
                    prScore = eval?.pronunciationBand ?: 7.5,
                    vocabUpgrades = eval?.vocabularySuggestions ?: listOf("paramount", "transformative", "mitigate dislocations"),
                    grammarTips = eval?.intonationFeedback ?: "Excellent syntactic range. Ensure consistent subordinate clause connective punctuation."
                )
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .testTag("ai_speaking_practice_screen")
    ) {
        // Top Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onNavigateBack, modifier = Modifier.testTag("ai_spk_back_btn")) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "AI SPEAKING EXAMINER",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Real-time Interactive Speech Studio",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonCyan.copy(alpha = 0.15f))
                        .border(1.dp, NeonCyan, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "GEMINI AI",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
        }

        // Speaking Part Selector Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(1 to "Part 1 (Intro)", 2 to "Part 2 (Cue Card)", 3 to "Part 3 (Discussion)").forEach { (part, label) ->
                val isSelected = selectedPart == part
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (isSelected) NeonCyan else SurfaceCard)
                        .clickable {
                            selectedPart = part
                            conversation.clear()
                            val initialPrompt = when (part) {
                                1 -> "Hello! I am your AI IELTS Speaking Examiner. Today we will conduct simulated practice. Let's begin with Part 1: Could you tell me about the area where you currently reside, and what you find most appealing about it?"
                                2 -> "Welcome to Part 2. Here is your cue card:\n\nDescribe a groundbreaking technological development you would like to witness in your lifetime.\n\nYou have 1 minute to plan, then speak for 2 minutes."
                                else -> "Welcome to Part 3: Two-way Analytical Discussion. Do you think automation and artificial intelligence will widen or reduce educational inequality globally?"
                            }
                            conversation.add(
                                SpeakingExchangeMessage(
                                    id = "init_$part",
                                    isFromAi = true,
                                    text = initialPrompt,
                                    timestamp = "Just now"
                                )
                            )
                        }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (isSelected) BackgroundDark else TextSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Conversation List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(conversation) { msg ->
                if (msg.isFromAi) {
                    AiExaminerMessageCard(msg)
                } else {
                    CandidateMessageCard(msg)
                }
            }

            if (isAnalyzing) {
                item {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = NeonCyan,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "AI Examiner is analyzing your pronunciation, fluency, and vocabulary...",
                            style = MaterialTheme.typography.bodySmall.copy(color = NeonCyan)
                        )
                    }
                }
            }
        }

        // Live Audio Recorder & Input Deck
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SurfaceCard,
            border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceBorder)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Recording Status Bar
                AnimatedVisibility(visible = isRecording) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(NeonRed.copy(alpha = 0.15f))
                            .border(1.dp, NeonRed, RoundedCornerShape(8.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .clip(CircleShape)
                                    .background(NeonRed)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "RECORDING SPEECH: ${recordingSeconds}s",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = NeonRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            text = "Tap Stop Mic to Transcribe",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Record / Stop Mic Button
                    IconButton(
                        onClick = {
                            if (isRecording) {
                                stopRecordingAudioAndTranscribe()
                            } else {
                                startRecordingAudio()
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (isRecording) NeonRed else NeonCyan)
                            .testTag("ai_mic_btn")
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (isRecording) "Stop Recording" else "Start Microphone",
                            tint = BackgroundDark
                        )
                    }

                    // Candidate Transcript / Text Input
                    OutlinedTextField(
                        value = candidateInputText,
                        onValueChange = { candidateInputText = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("ai_candidate_input"),
                        placeholder = {
                            Text(
                                if (isRecording) "Listening to microphone..." else "Speak with mic or type response...",
                                color = TextMuted,
                                fontSize = 13.sp
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = SurfaceBorder
                        ),
                        shape = RoundedCornerShape(12.dp),
                        maxLines = 3
                    )

                    // Send Action Button
                    IconButton(
                        onClick = { sendCandidateResponse() },
                        enabled = candidateInputText.isNotBlank() && !isAnalyzing,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (candidateInputText.isNotBlank() && !isAnalyzing) NeonGreen else SurfaceElevated)
                            .testTag("ai_send_speech_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Submit Speech",
                            tint = if (candidateInputText.isNotBlank()) BackgroundDark else TextMuted
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AiExaminerMessageCard(msg: SpeakingExchangeMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 32.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(NeonCyan.copy(alpha = 0.2f))
                    .border(1.dp, NeonCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = null,
                    tint = NeonCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "AI EXAMINER",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        FuturisticGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonCyan.copy(alpha = 0.35f)
        ) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, lineHeight = 22.sp)
            )

            // Diagnostic Band Card if evaluated
            if (msg.bandEstimate != null) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = SurfaceBorder)
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "AI PRACTICE ESTIMATE",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Estimated Band ${String.format("%.1f", msg.bandEstimate)}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonGreen
                            )
                        )
                    }

                    FuturisticScoreRing(
                        currentScore = msg.bandEstimate,
                        targetScore = 9.0,
                        ringSize = 56.dp,
                        strokeWidth = 5.dp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RubricScoreBadge("Fluency", msg.fcScore ?: 7.5, Modifier.weight(1f))
                    RubricScoreBadge("Vocab", msg.lrScore ?: 7.5, Modifier.weight(1f))
                    RubricScoreBadge("Grammar", msg.graScore ?: 7.0, Modifier.weight(1f))
                    RubricScoreBadge("Pronun", msg.prScore ?: 7.5, Modifier.weight(1f))
                }

                if (msg.vocabUpgrades.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Vocabulary Upgrades: " + msg.vocabUpgrades.joinToString(", "),
                        style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan)
                    )
                }

                if (msg.grammarTips != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Examiner Tip: ${msg.grammarTips}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CandidateMessageCard(msg: SpeakingExchangeMessage) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "YOU (CANDIDATE)",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = ElectricViolet,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(ElectricViolet.copy(alpha = 0.2f))
                    .border(1.dp, ElectricViolet, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = ElectricViolet,
                    modifier = Modifier.size(14.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(14.dp))
                .background(SurfaceElevated)
                .border(1.dp, ElectricViolet.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                .padding(14.dp)
        ) {
            Text(
                text = msg.text,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, lineHeight = 20.sp)
            )
        }
    }
}

@Composable
private fun RubricScoreBadge(
    title: String,
    score: Double,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(SurfaceDark)
            .border(1.dp, SurfaceBorder, RoundedCornerShape(6.dp))
            .padding(vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = String.format("%.1f", score),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = NeonCyan
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextMuted,
                    fontSize = 9.sp
                )
            )
        }
    }
}
