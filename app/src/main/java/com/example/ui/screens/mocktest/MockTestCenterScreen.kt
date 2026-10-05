package com.example.ui.screens.mocktest

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
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
import com.example.data.model.MockTest
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun MockTestCenterScreen(
    onStartTest: (String) -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val mockTests by repository.mockTests.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Available, 1 = Completed

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("mock_test_center_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Text(
                text = "MOCK TEST CENTER",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            )
            Text(
                text = "Full-Length Exam Engine",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Database-driven mock tests mirroring official British Council & IDP exam timings and scoring rubrics.",
                style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
            )
        }

        // Filter Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Available Tests (${mockTests.size})", "Completed Tests (1)").forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonCyan else SurfaceCard)
                            .clickable { selectedTab = index }
                            .testTag("mock_tab_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BackgroundDark else TextSecondary
                            )
                        )
                    }
                }
            }
        }

        // Test Cards List
        items(mockTests) { test ->
            MockTestCenterCard(
                mockTest = test,
                onStart = { onStartTest(test.id) }
            )
        }

        // Content Licensing & Compliance Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SurfaceBorder
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "ACADEMY LICENSING & ETHICAL INTEGRITY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonCyan,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "All mock test materials in this platform are original proprietary questions developed by Dawood Bhai IELTS Studio & Sahil VANTIS, or verified public-domain pedagogical sources under academic license. Practice bands are diagnostic estimates and do not substitute official IELTS test results.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun MockTestCenterCard(
    mockTest: MockTest,
    onStart: () -> Unit
) {
    FuturisticGlassCard(
        modifier = Modifier.fillMaxWidth(),
        borderColor = if (mockTest.isCompleted) NeonGreen.copy(alpha = 0.4f) else NeonCyan.copy(alpha = 0.35f),
        testTag = "test_item_${mockTest.id}"
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
                    text = mockTest.ieltsType.displayName,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            if (mockTest.isCompleted && mockTest.bestEstimatedBand != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Completed: Band ${String.format("%.1f", mockTest.bestEstimatedBand)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ElectricViolet.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = mockTest.difficulty,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = ElectricViolet,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = mockTest.title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Schedule, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${mockTest.durationMinutes} mins", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("${mockTest.totalQuestions} Questions", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Owner: ${mockTest.copyrightOwner} • ${mockTest.licenseStatus}",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
        )

        Spacer(modifier = Modifier.height(14.dp))

        FuturisticGlowButton(
            text = if (mockTest.isCompleted) "RE-TAKE MOCK TEST" else "START MOCK TEST",
            onClick = onStart,
            modifier = Modifier.fillMaxWidth(),
            containerColor = if (mockTest.isCompleted) ElectricViolet else NeonCyan,
            contentColor = if (mockTest.isCompleted) Color.White else BackgroundDark,
            testTag = "start_test_btn_${mockTest.id}"
        )
    }
}
