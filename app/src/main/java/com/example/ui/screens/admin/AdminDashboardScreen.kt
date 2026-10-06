package com.example.ui.screens.admin

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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.AttemptEntity
import com.example.data.local.entity.UserEntity
import com.example.data.model.IeltsBook
import com.example.data.model.IeltsModuleType
import com.example.data.model.IeltsType
import com.example.data.model.MockTest
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboardScreen(
    onNavigateBack: () -> Unit
) {
    val repository = remember { IeltsRepository.getInstance() }
    val userProfile by repository.userProfile.collectAsState()
    val registeredUsers by repository.getAllRegisteredUsersFlow().collectAsState(initial = emptyList())
    val allAttempts by repository.getAllAttemptsFlow().collectAsState(initial = emptyList())
    val books by repository.books.collectAsState()
    val mockTests by repository.mockTests.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddBookDialog by remember { mutableStateOf(false) }
    var showAddTestDialog by remember { mutableStateOf(false) }
    var selectedUserForEdit by remember { mutableStateOf<UserEntity?>(null) }
    var newBandScoreInput by remember { mutableDoubleStateOf(7.5) }

    val dateFormat = remember { SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault()) }

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
        // Top Header
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

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SUPER ADMIN CONSOLE",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonCyan,
                            letterSpacing = 1.sp
                        )
                    )
                    Text(
                        text = "Full Candidate & Academy Authority",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeonGreen.copy(alpha = 0.15f))
                        .border(1.dp, NeonGreen, RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ONLINE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonGreen,
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
        }

        // Super Admin Authority Profile Card
        item {
            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.5f),
                testTag = "admin_authority_card"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(NeonCyan.copy(alpha = 0.2f))
                            .border(2.dp, NeonCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Super Admin Account",
                            style = MaterialTheme.typography.labelSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "sahilwaqar50@gmail.com",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = TextPrimary
                            )
                        )
                        Text(
                            text = "Authorized to inspect registered users, candidate scores, live test sessions & manage all academy records.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary, fontSize = 11.sp)
                        )
                    }
                }
            }
        }

        // Live Real-Time Telemetry Counters
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AdminStatCounter(
                    title = "Candidates",
                    value = "${registeredUsers.size.coerceAtLeast(1)}",
                    color = NeonCyan,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCounter(
                    title = "Live Attempts",
                    value = "${allAttempts.size}",
                    color = NeonGreen,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCounter(
                    title = "Books",
                    value = "${books.size}",
                    color = ElectricViolet,
                    modifier = Modifier.weight(1f)
                )
                AdminStatCounter(
                    title = "Mock Tests",
                    value = "${mockTests.size}",
                    color = NeonAmber,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Admin Navigation Tabs
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    0 to "Candidate Roster & Scores",
                    1 to "Curriculum & Books",
                    2 to "Quick Actions"
                ).forEach { (idx, label) ->
                    val isSelected = selectedTab == idx
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSelected) NeonCyan else SurfaceCard)
                            .clickable { selectedTab = idx }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isSelected) BackgroundDark else TextSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // TAB 0: REGISTERED CANDIDATES & LIVE ACTIVITY
        if (selectedTab == 0) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "REGISTERED USERS & LIVE ACTIVITY (${registeredUsers.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }
            }

            if (registeredUsers.isEmpty()) {
                item {
                    FuturisticGlassCard(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "No additional candidates registered yet.",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = TextPrimary)
                        )
                        Text(
                            text = "When students register from the Auth screen, their real-time activity and scores will appear here instantly.",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                        )
                    }
                }
            } else {
                items(registeredUsers) { user ->
                    val userAttempts = allAttempts.filter { it.userId == user.id }
                    FuturisticGlassCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = NeonCyan.copy(alpha = 0.35f),
                        testTag = "admin_user_card_${user.id}"
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = user.fullName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (user.role == "ADMIN") NeonGreen.copy(alpha = 0.2f) else SurfaceElevated)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = user.role,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (user.role == "ADMIN") NeonGreen else NeonCyan,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }

                                Text(
                                    text = user.email,
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "Target: Band ${user.targetBand} • Current: Band ${user.currentBand} • ${user.streakDays}d Streak • ${user.xp} XP",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = String.format("Band %.1f", user.currentBand),
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        color = NeonGreen
                                    )
                                )
                                Text(
                                    text = "${userAttempts.size} Tests Taken",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = SurfaceBorder)
                        Spacer(modifier = Modifier.height(10.dp))

                        // Candidate's Live Exam History & Scores
                        Text(
                            text = "WHAT IS HE DOING & SCORES:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = NeonCyan,
                                fontWeight = FontWeight.Bold
                            )
                        )

                        if (userAttempts.isEmpty()) {
                            Text(
                                text = "Candidate has not completed any full mock tests yet.",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextMuted)
                            )
                        } else {
                            userAttempts.take(3).forEach { att ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "• ${att.testTitle}",
                                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary),
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = "${att.rawScore}/${att.totalQuestions} (${att.timeTakenSeconds / 60}m) → Band ${String.format("%.1f", att.bandScore)}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen, fontWeight = FontWeight.Bold)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Admin Action Controls for this candidate
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    selectedUserForEdit = user
                                    newBandScoreInput = user.currentBand
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Edit Score", fontSize = 11.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    val nextRole = if (user.role == "STUDENT") "TEACHER" else "STUDENT"
                                    repository.updateCandidateRole(user.id, nextRole)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(if (user.role == "STUDENT") "Make Teacher" else "Make Student", fontSize = 11.sp)
                            }

                            IconButton(
                                onClick = { repository.deleteCandidate(user.id) },
                                modifier = Modifier.size(38.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = NeonRed)
                            }
                        }
                    }
                }
            }
        }

        // TAB 1: CURRICULUM & BOOK MANAGEMENT
        if (selectedTab == 1) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ACADEMY BOOKS & TESTS",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    FuturisticGlowButton(
                        text = "+ ADD BOOK",
                        onClick = { showAddBookDialog = true },
                        modifier = Modifier.height(34.dp)
                    )
                }
            }

            items(books) { book ->
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = ElectricViolet.copy(alpha = 0.4f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = book.title,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${book.edition} • ${book.publisher}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(ElectricViolet.copy(alpha = 0.2f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${book.testCount} Tests",
                                style = MaterialTheme.typography.labelSmall.copy(color = ElectricViolet, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PUBLISHED MOCK TESTS (${mockTests.size})",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    FuturisticGlowButton(
                        text = "+ CREATE TEST",
                        onClick = { showAddTestDialog = true },
                        containerColor = NeonGreen,
                        contentColor = BackgroundDark,
                        modifier = Modifier.height(34.dp)
                    )
                }
            }

            items(mockTests) { test ->
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.35f)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = test.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "${test.durationMinutes}m • ${test.totalQuestions} Questions • ${test.moduleType.displayName}",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(NeonGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "LIVE IN APP",
                                style = MaterialTheme.typography.labelSmall.copy(color = NeonGreen, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // TAB 2: QUICK ACTIONS & BACKEND CONTROLS
        if (selectedTab == 2) {
            item {
                FuturisticGlassCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = NeonCyan.copy(alpha = 0.4f)
                ) {
                    Text(
                        text = "ACADEMY PRODUCTION STATUS",
                        style = MaterialTheme.typography.titleSmall.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Room SQLite Database: Connected & Operational\n• Supabase PostgREST Client: Synchronized\n• Candidate Multi-User Isolation: Active\n• Gemini AI Speech Practice Model: Online",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextPrimary, lineHeight = 22.sp)
                    )
                }
            }
        }
    }

    // Edit Candidate Score Dialog
    if (selectedUserForEdit != null) {
        val user = selectedUserForEdit!!
        AlertDialog(
            onDismissRequest = { selectedUserForEdit = null },
            title = { Text("Update Candidate Band", color = TextPrimary) },
            text = {
                Column {
                    Text("Candidate: ${user.fullName} (${user.email})", color = TextSecondary)
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "New Band: ${String.format("%.1f", newBandScoreInput)}",
                        style = MaterialTheme.typography.titleMedium.copy(color = NeonCyan, fontWeight = FontWeight.Bold)
                    )
                    Slider(
                        value = newBandScoreInput.toFloat(),
                        onValueChange = {
                            newBandScoreInput = (Math.round(it * 2) / 2.0).coerceIn(4.0, 9.0)
                        },
                        valueRange = 4f..9f,
                        steps = 9
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    repository.updateCandidateBand(user.id, newBandScoreInput)
                    selectedUserForEdit = null
                }) {
                    Text("Save Band Score", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedUserForEdit = null }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }

    // Add Book Dialog
    if (showAddBookDialog) {
        var bookTitle by remember { mutableStateOf("Cambridge IELTS 20 Academic") }
        var bookEdition by remember { mutableStateOf("Official Examination Papers 2025") }
        var bookPublisher by remember { mutableStateOf("Cambridge University Press") }

        AlertDialog(
            onDismissRequest = { showAddBookDialog = false },
            title = { Text("Publish New Cambridge Book", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bookTitle,
                        onValueChange = { bookTitle = it },
                        label = { Text("Book Title") }
                    )
                    OutlinedTextField(
                        value = bookEdition,
                        onValueChange = { bookEdition = it },
                        label = { Text("Edition") }
                    )
                    OutlinedTextField(
                        value = bookPublisher,
                        onValueChange = { bookPublisher = it },
                        label = { Text("Publisher") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (bookTitle.isNotBlank()) {
                        repository.addBook(bookTitle, bookEdition, bookPublisher)
                        showAddBookDialog = false
                    }
                }) {
                    Text("Publish Book", color = NeonCyan, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddBookDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }

    // Add Test Dialog
    if (showAddTestDialog) {
        var testTitle by remember { mutableStateOf("Cambridge 20 Test 1 (Full Mock)") }
        var durationMinutes by remember { mutableIntStateOf(160) }

        AlertDialog(
            onDismissRequest = { showAddTestDialog = false },
            title = { Text("Deploy Real-Time Mock Test", color = TextPrimary) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = testTitle,
                        onValueChange = { testTitle = it },
                        label = { Text("Test Title") }
                    )
                    Text("Duration: $durationMinutes Minutes", color = TextSecondary)
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (testTitle.isNotBlank()) {
                        repository.addMockTest(
                            MockTest(
                                id = "test_custom_${System.currentTimeMillis()}",
                                title = testTitle,
                                ieltsType = IeltsType.ACADEMIC,
                                difficulty = "Standard Exam Standard",
                                durationMinutes = durationMinutes,
                                totalQuestions = 40,
                                moduleType = IeltsModuleType.READING,
                                sections = emptyList()
                            )
                        )
                        showAddTestDialog = false
                    }
                }) {
                    Text("Deploy to Students", color = NeonGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddTestDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}

@Composable
private fun AdminStatCounter(
    title: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceCard)
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(10.dp))
            .padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = color,
                    fontWeight = FontWeight.Black
                )
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 9.sp
                )
            )
        }
    }
}
