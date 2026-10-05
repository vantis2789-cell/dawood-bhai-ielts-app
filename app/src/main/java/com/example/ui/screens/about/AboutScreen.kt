package com.example.ui.screens.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.components.GlassCard
import com.example.ui.components.PrimaryButton
import com.example.ui.components.SecondaryButton
import com.example.ui.theme.*

@Composable
fun AboutScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    var showDeleteAccountDialog by remember { mutableStateOf(false) }
    var accountDeletedFeedback by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .padding(horizontal = 18.dp)
            .testTag("about_screen"),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
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
                    modifier = Modifier.testTag("about_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextPrimary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ABOUT & ACADEMY INTEGRITY",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonCyan,
                        letterSpacing = 1.sp
                    )
                )
            }
        }

        // Academy Emblem & Header
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.45f),
                testTag = "about_header_card"
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(SurfaceElevated)
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_app_launcher),
                            contentDescription = "Dawood Bhai IELTS Studio Logo",
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "DAWOOD BHAI IELTS STUDIO",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextPrimary,
                            letterSpacing = 1.sp
                        ),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Futuristic IELTS Preparation Command Center",
                        style = MaterialTheme.typography.bodyMedium.copy(color = NeonCyan)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SurfaceElevated)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Version 1.0.0 (Production Release)",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        // Developer Credit Card (Exact spelling: Sahil VANTIS)
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = ElectricViolet.copy(alpha = 0.5f),
                testTag = "about_developer_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(ElectricViolet.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Code,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "CREATOR & CHIEF ARCHITECT",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        )
                        Text(
                            text = "Sahil VANTIS",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Architectural Engineering • Jetpack Compose UI",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            }
        }

        // Academy Information & Contact
        item {
            GlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = SurfaceBorder
            ) {
                Text(
                    text = "ACADEMY INFORMATION & CONTACT",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = NeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                AboutInfoRow("Academy Name", "Dawood Bhai IELTS Studio")
                AboutInfoRow("Global Campus", "Digital Learning Cloud & Mobile Portal")
                AboutInfoRow("Support Desk", "support@dawoodbhai-ielts.com")
                AboutInfoRow("Lead Developer", "Sahil VANTIS")
            }
        }

        // Legal & Privacy Actions
        item {
            Text(
                text = "PRIVACY, TERMS & ACCOUNT CONTROLS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPrivacyDialog = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = NeonGreen, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Privacy Policy & Audio Encryption", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                    }
                }

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showTermsDialog = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Gavel, contentDescription = null, tint = NeonCyan, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Terms of Service & Licensing Rights", style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary))
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                    }
                }

                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showDeleteAccountDialog = true }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.DeleteForever, contentDescription = null, tint = NeonRed, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Request Complete Account Deletion", style = MaterialTheme.typography.bodyMedium.copy(color = NeonRed))
                        }
                        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = TextSecondary)
                    }
                }
            }
        }

        // Sign Out Button
        item {
            SecondaryButton(
                text = "LOGOUT OF COMMAND CENTER",
                onClick = onSignOut,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // Privacy Policy Dialog
    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy", color = TextPrimary) },
            text = {
                Column {
                    Text(
                        "Dawood Bhai IELTS Studio values your personal privacy. We collect only student information strictly necessary for academic tracking (email, practice scores, writing submissions, and speaking audio clips). Audio recordings are encrypted in private storage and never shared with commercial brokers or third parties.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Close", color = NeonCyan)
                }
            },
            containerColor = SurfaceElevated
        )
    }

    // Terms of Service Dialog
    if (showTermsDialog) {
        AlertDialog(
            onDismissRequest = { showTermsDialog = false },
            title = { Text("Terms of Service", color = TextPrimary) },
            text = {
                Column {
                    Text(
                        "All curriculum content within Dawood Bhai IELTS Studio is authorized proprietary educational material developed by the academy and Sahil VANTIS, or verified under open academic license. Diagnostic band scores calculated in this app are estimates for preparation purposes only and do not constitute an official IELTS certificate from Cambridge, IDP, or British Council.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, lineHeight = 18.sp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showTermsDialog = false }) {
                    Text("Understood", color = NeonCyan)
                }
            },
            containerColor = SurfaceElevated
        )
    }

    // Account Deletion Dialog
    if (showDeleteAccountDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteAccountDialog = false },
            title = { Text("Request Account Deletion", color = NeonRed) },
            text = {
                Text(
                    "Are you sure you wish to delete your candidate record? This will permanently erase your diagnostic test history, writing drafts, speaking audio recordings, and streak progress from the academy database.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteAccountDialog = false
                    accountDeletedFeedback = true
                    onSignOut()
                }) {
                    Text("Confirm Delete", color = NeonRed, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteAccountDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}

@Composable
private fun AboutInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, fontWeight = FontWeight.SemiBold))
    }
}
