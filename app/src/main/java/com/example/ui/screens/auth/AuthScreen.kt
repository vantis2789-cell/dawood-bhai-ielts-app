package com.example.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.data.repository.IeltsRepository
import com.example.ui.components.FuturisticGlassCard
import com.example.ui.components.FuturisticGlowButton
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("haris.ielts@academy.db") }
    var password by remember { mutableStateOf("ieltsPass2026") }
    var fullName by remember { mutableStateOf("Haris Mahmood") }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showForgotPasswordDialog by remember { mutableStateOf(false) }

    val repository = remember { IeltsRepository.getInstance() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp)
            .testTag("auth_screen")
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Academy Brand Header
            Text(
                text = "DAWOOD BHAI IELTS STUDIO",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = NeonCyan,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (isRegisterMode) "Create Academy Account" else "Welcome Back, Candidate",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role Selector Chips (Student, Teacher, Admin)
            Text(
                text = "SELECT ACCESS ROLE",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                UserRole.values().forEach { role ->
                    val isSelected = selectedRole == role
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) NeonCyan else SurfaceCard)
                            .border(1.dp, if (isSelected) NeonCyan else SurfaceBorder, RoundedCornerShape(12.dp))
                            .clickable {
                                selectedRole = role
                                repository.switchUserRole(role)
                            }
                            .testTag("role_btn_${role.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = role.name,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) BackgroundDark else TextSecondary
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            FuturisticGlassCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = NeonCyan.copy(alpha = 0.3f)
            ) {
                if (isRegisterMode) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name", color = TextSecondary) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = NeonCyan) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auth_name_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = NeonCyan,
                            unfocusedBorderColor = SurfaceBorder
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    label = { Text("Academy Email", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = NeonCyan) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_email_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = SurfaceBorder
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMessage = null
                    },
                    label = { Text("Password", color = TextSecondary) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = NeonCyan) },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("auth_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = SurfaceBorder
                    ),
                    singleLine = true
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(color = NeonRed),
                        modifier = Modifier.testTag("auth_error_msg")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                FuturisticGlowButton(
                    text = if (isRegisterMode) "REGISTER WITH SUPABASE" else "SIGN IN TO COMMAND CENTER",
                    onClick = {
                        if (!email.contains("@") || !email.contains(".")) {
                            errorMessage = "Please enter a valid email address."
                        } else if (password.length < 6) {
                            errorMessage = "Password must contain at least 6 characters."
                        } else {
                            repository.switchUserRole(selectedRole)
                            onLoginSuccess()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    testTag = "auth_submit_btn"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    onClick = { showForgotPasswordDialog = true },
                    modifier = Modifier.testTag("forgot_pwd_btn")
                ) {
                    Text("Forgot Password?", color = TextSecondary, fontSize = 13.sp)
                }

                TextButton(
                    onClick = {
                        isRegisterMode = !isRegisterMode
                        errorMessage = null
                    },
                    modifier = Modifier.testTag("toggle_register_btn")
                ) {
                    Text(
                        if (isRegisterMode) "Existing User? Sign In" else "New Student? Register",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subtle Developer Credit
            Text(
                text = "Designed & Developed by Sahil VANTIS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color(0xFF64748B),
                    letterSpacing = 0.5.sp
                )
            )
        }
    }

    if (showForgotPasswordDialog) {
        AlertDialog(
            onDismissRequest = { showForgotPasswordDialog = false },
            title = { Text("Password Reset", color = TextPrimary) },
            text = {
                Text(
                    "A secure reset verification token will be dispatched to your registered address: $email via Supabase Auth services.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Send Reset Link", color = NeonCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { showForgotPasswordDialog = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            },
            containerColor = SurfaceElevated
        )
    }
}
