package com.example.ui.screens.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IeltsType
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit
) {
    var step by remember { mutableIntStateOf(1) }
    var selectedType by remember { mutableStateOf(IeltsType.ACADEMIC) }
    var selectedTargetBand by remember { mutableDoubleStateOf(7.5) }
    var selectedDaysLeft by remember { mutableIntStateOf(42) }
    var selectedDailyMinutes by remember { mutableIntStateOf(60) }

    val repository = remember { IeltsRepository.getInstance() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(20.dp)
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Step Progress Indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STEP $step OF 6",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                )

                // Dots
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    for (i in 1..6) {
                        Box(
                            modifier = Modifier
                                .size(if (i == step) 18.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (i == step) NeonCyan else Color(0xFF1E293B))
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Step Content
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith
                            slideOutHorizontally { -it } + fadeOut()
                },
                label = "onboarding_steps",
                modifier = Modifier.weight(1f)
            ) { currentStep ->
                when (currentStep) {
                    1 -> Step1Welcome()
                    2 -> Step2IeltsType(selectedType) { selectedType = it }
                    3 -> Step3TargetBand(selectedTargetBand) { selectedTargetBand = it }
                    4 -> Step4ExamDate(selectedDaysLeft) { selectedDaysLeft = it }
                    5 -> Step5DailyStudyTime(selectedDailyMinutes) { selectedDailyMinutes = it }
                    6 -> Step6BuildJourney(selectedType, selectedTargetBand, selectedDaysLeft, selectedDailyMinutes)
                }
            }

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (step > 1) {
                    TextButton(
                        onClick = { step-- },
                        modifier = Modifier.testTag("onboarding_back_btn")
                    ) {
                        Text("Back", color = TextSecondary)
                    }
                } else {
                    Spacer(modifier = Modifier.width(60.dp))
                }

                FuturisticGlowButton(
                    text = if (step == 6) "LAUNCH COMMAND CENTER" else "CONTINUE",
                    onClick = {
                        if (step < 6) {
                            step++
                        } else {
                            repository.updateProfile(selectedTargetBand, selectedType, selectedDailyMinutes)
                            onComplete()
                        }
                    },
                    testTag = "onboarding_next_btn"
                )
            }
        }
    }
}

@Composable
private fun Step1Welcome() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(SurfaceElevated)
                .border(2.dp, NeonCyan, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Welcome to",
            style = MaterialTheme.typography.titleMedium.copy(color = TextSecondary)
        )
        Text(
            text = "DAWOOD BHAI IELTS STUDIO",
            style = MaterialTheme.typography.headlineLarge.copy(
                color = NeonCyan,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "Your personal futuristic IELTS command center. Designed to guide you systematically from your current diagnostics to Band 7.5+.",
            style = MaterialTheme.typography.bodyLarge.copy(
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            ),
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun Step2IeltsType(
    currentSelection: IeltsType,
    onSelect: (IeltsType) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Which IELTS test are you preparing for?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Select your target qualification stream.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(28.dp))

        listOf(
            IeltsType.ACADEMIC to "For university admissions, postgraduate degrees, and professional certifications.",
            IeltsType.GENERAL_TRAINING to "For migration (Express Entry, Australian PR), secondary education, and employment."
        ).forEach { (type, description) ->
            val isSelected = currentSelection == type
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onSelect(type) },
                borderColor = if (isSelected) NeonCyan else SurfaceBorder,
                backgroundColor = if (isSelected) SurfaceElevated else SurfaceCard,
                testTag = "type_card_${type.name}"
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(type) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = NeonCyan,
                            unselectedColor = Color(0xFF64748B)
                        )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = type.displayName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonCyan else TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step3TargetBand(
    selectedBand: Double,
    onSelect: (Double) -> Unit
) {
    val bands = listOf(6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "What's your target band?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Our neural learning engine customizes your daily mission based on this goal.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(bands) { band ->
                val isSelected = selectedBand == band
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) NeonCyan else SurfaceCard)
                        .border(
                            1.dp,
                            if (isSelected) NeonCyan else SurfaceBorder,
                            RoundedCornerShape(16.dp)
                        )
                        .clickable { onSelect(band) }
                        .testTag("target_band_${band}"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = String.format("%.1f", band),
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) BackgroundDark else TextPrimary
                            )
                        )
                        Text(
                            text = if (band >= 8.0) "Expert" else if (band >= 7.0) "Very Good" else "Competent",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) BackgroundDark.copy(alpha = 0.8f) else TextSecondary
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step4ExamDate(
    daysLeft: Int,
    onDaysChange: (Int) -> Unit
) {
    val options = listOf(14 to "2 Weeks (Express Crash)", 30 to "1 Month (Intensive)", 42 to "6 Weeks (Recommended)", 90 to "3 Months (Comprehensive)")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "When are you planning to take IELTS?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "We will calibrate your countdown timer and revision velocity.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        options.forEach { (days, label) ->
            val isSelected = daysLeft == days
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { onDaysChange(days) },
                borderColor = if (isSelected) NeonCyan else SurfaceBorder,
                backgroundColor = if (isSelected) SurfaceElevated else SurfaceCard,
                testTag = "exam_days_${days}"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "$days Days Countdown",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) NeonCyan else TextPrimary
                            )
                        )
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                        )
                    }
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step5DailyStudyTime(
    dailyMinutes: Int,
    onSelectMinutes: (Int) -> Unit
) {
    val durations = listOf(15 to "15 minutes (Bite-sized)", 30 to "30 minutes (Moderate)", 60 to "1 hour (Recommended)", 120 to "2 hours (Intensive)", 180 to "3+ hours (Full Immersion)")

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "How much time can you study every day?",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Consistency unlocks higher band retention and active recall.",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(24.dp))

        durations.forEach { (min, label) ->
            val isSelected = dailyMinutes == min
            FuturisticGlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { onSelectMinutes(min) },
                borderColor = if (isSelected) NeonCyan else SurfaceBorder,
                backgroundColor = if (isSelected) SurfaceElevated else SurfaceCard,
                testTag = "study_time_${min}"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) NeonCyan else TextPrimary
                        )
                    )
                    if (isSelected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = NeonCyan
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Step6BuildJourney(
    type: IeltsType,
    targetBand: Double,
    daysLeft: Int,
    dailyMinutes: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(SurfaceElevated)
                .border(2.dp, ElectricViolet, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.RocketLaunch,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Let's build your IELTS journey.",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Black,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your personalized Command Center profile is ready:",
            style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
        )

        Spacer(modifier = Modifier.height(20.dp))

        FuturisticGlassCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = NeonCyan.copy(alpha = 0.4f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Qualification:", color = TextSecondary)
                Text(type.displayName, color = NeonCyan, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Target Band:", color = TextSecondary)
                Text(String.format("%.1f", targetBand), color = ElectricViolet, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Exam Timeline:", color = TextSecondary)
                Text("$daysLeft Days Left", color = TextPrimary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Daily Study Target:", color = TextSecondary)
                Text("$dailyMinutes Minutes", color = NeonGreen, fontWeight = FontWeight.Bold)
            }
        }
    }
}
