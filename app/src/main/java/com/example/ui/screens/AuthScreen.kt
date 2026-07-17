package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AuthScreen(
    userEmail: String?,
    isSyncing: Boolean,
    isAuthLoading: Boolean,
    authErrorMessage: String?,
    localCheckInsCount: Int,
    localBreathCount: Int,
    onSignUp: (String, String) -> Unit,
    onLogin: (String, String) -> Unit,
    onSocialLogin: (String, String) -> Unit,
    onLogout: () -> Unit,
    onSync: () -> Unit,
    onTriggerSampleException: (String) -> Unit,
    onTriggerNotification: (String, String) -> Unit
) {
    var isRegisterMode by remember { mutableStateOf(false) }
    var emailInput by remember { mutableStateOf("Monika.Krzyszton@amone.com") }
    var passwordInput by remember { mutableStateOf("Amone123!") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Diagnostic inputs
    var sentryMessage by remember { mutableStateOf("Testowy błąd somatyczny") }
    var pushTitle by remember { mutableStateOf("AMONE HOS") }
    var pushBody by remember { mutableStateOf("Czas na 5 głębokich oddechów regulacyjnych.") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        // Large icon
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(GoldPremium.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = null,
                tint = GoldPremium,
                modifier = Modifier.size(36.dp)
            )
        }

        Text(
            text = "Konto & Synchronizacja",
            color = PearlWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Uzyskaj pełny dostęp do bazy chmurowej AMONE za pomocą Firebase Auth. Synchronizuj sesje oddechowe, skany ciała i cykle hormonalne.",
            color = GrayText,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        // Error Banner
        if (authErrorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Error icon",
                        tint = MaterialTheme.colorScheme.onErrorContainer
                    )
                    Text(
                        text = authErrorMessage,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 12.sp
                    )
                }
            }
        }

        if (userEmail != null) {
            // --- Logged in Card ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, GoldPremium)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = GoldPremium,
                        modifier = Modifier.size(48.dp)
                    )

                    Text(
                        text = "Zalogowano jako:",
                        color = GrayText,
                        fontSize = 12.sp
                    )

                    Text(
                        text = userEmail,
                        color = PearlWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    HorizontalDivider(color = BorderGlass)

                    // Local stats preview
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = localCheckInsCount.toString(), color = VioletSoft, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Skanów Ciała", color = GrayText, fontSize = 11.sp)
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = localBreathCount.toString(), color = BlueSoft, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            Text(text = "Sesji Oddechowych", color = GrayText, fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sync button
                    Button(
                        onClick = onSync,
                        enabled = !isSyncing,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPremium),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("sync_cloud_button")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isSyncing) Icons.Default.CloudSync else Icons.Default.CloudDone,
                                contentDescription = null,
                                tint = DarkBgBlack
                            )
                            Text(
                                text = if (isSyncing) "Synchronizacja w chmurze..." else "Zsynchronizuj z Firestore",
                                color = DarkBgBlack,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Logout button
                    Button(
                        onClick = onLogout,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        border = BorderStroke(1.dp, BorderGlass),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("logout_button")
                    ) {
                        Text(text = "Wyloguj się", color = GrayText, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        } else {
            // --- Login/Register Form ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, BorderGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Header / Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isRegisterMode) "Utwórz Nowe Konto" else "Formularz Logowania",
                            color = PearlWhite,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isRegisterMode) "Zaloguj się" else "Zarejestruj",
                            color = GoldPremium,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .clickable { isRegisterMode = !isRegisterMode }
                                .padding(4.dp)
                        )
                    }

                    // Email field
                    TextField(
                        value = emailInput,
                        onValueChange = { emailInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("email_auth_input"),
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = GrayText) },
                        placeholder = { Text("E-mail użytkownika", color = GrayText) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DarkBgBlack,
                            unfocusedContainerColor = DarkBgBlack,
                            focusedTextColor = PearlWhite,
                            unfocusedTextColor = PearlWhite,
                            focusedIndicatorColor = GoldPremium,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Password field
                    TextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("password_auth_input"),
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = GrayText) },
                        trailingIcon = {
                            val icon = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(icon, contentDescription = "Pokaż hasło", tint = GrayText)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        placeholder = { Text("Hasło", color = GrayText) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = DarkBgBlack,
                            unfocusedContainerColor = DarkBgBlack,
                            focusedTextColor = PearlWhite,
                            unfocusedTextColor = PearlWhite,
                            focusedIndicatorColor = GoldPremium,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Primary Auth Button
                    Button(
                        onClick = {
                            if (isRegisterMode) {
                                onSignUp(emailInput, passwordInput)
                            } else {
                                onLogin(emailInput, passwordInput)
                            }
                        },
                        enabled = !isAuthLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPremium),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("submit_auth_button")
                    ) {
                        if (isAuthLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = DarkBgBlack, strokeWidth = 2.dp)
                        } else {
                            Text(
                                text = if (isRegisterMode) "Zarejestruj się" else "Zaloguj się",
                                color = DarkBgBlack,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Social login divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGlass)
                        Text("lub zaloguj przez", color = GrayText, fontSize = 10.sp)
                        HorizontalDivider(modifier = Modifier.weight(1f), color = BorderGlass)
                    }

                    // Social buttons row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Google Login
                        Button(
                            onClick = { onSocialLogin("Google", emailInput) },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = BorderStroke(1.dp, BorderGlass),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBox,
                                    contentDescription = null,
                                    tint = GoldPremium,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text("Google", color = PearlWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        // Facebook Login
                        Button(
                            onClick = { onSocialLogin("Facebook", emailInput) },
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            border = BorderStroke(1.dp, BorderGlass),
                            shape = RoundedCornerShape(100.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = null,
                                    tint = Color(0xFF1877F2),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text("Facebook", color = PearlWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // --- SECTION: FCM Push Notification Diagnostic Panel ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = BorderStroke(1.dp, BorderGlass)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = BlueSoft)
                    Text(
                        text = "Test Powiadomień FCM",
                        color = PearlWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Symuluj nadejście powiadomienia systemowego Push w tle. Spowoduje to wygenerowanie powiadomienia na pasku stanu Androida.",
                    color = GrayText,
                    fontSize = 11.sp
                )

                TextField(
                    value = pushTitle,
                    onValueChange = { pushTitle = it },
                    placeholder = { Text("Tytuł powiadomienia", color = GrayText) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkBgBlack,
                        unfocusedContainerColor = DarkBgBlack,
                        focusedTextColor = PearlWhite,
                        unfocusedTextColor = PearlWhite,
                        focusedIndicatorColor = BlueSoft,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                TextField(
                    value = pushBody,
                    onValueChange = { pushBody = it },
                    placeholder = { Text("Treść wiadomości", color = GrayText) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkBgBlack,
                        unfocusedContainerColor = DarkBgBlack,
                        focusedTextColor = PearlWhite,
                        unfocusedTextColor = PearlWhite,
                        focusedIndicatorColor = BlueSoft,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = { onTriggerNotification(pushTitle, pushBody) },
                    colors = ButtonDefaults.buttonColors(containerColor = BlueSoft),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                ) {
                    Text("Wyślij powiadomienie testowe", color = DarkBgBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // --- SECTION: Sentry Exception/Crash Diagnostic Panel ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = BorderStroke(1.dp, BorderGlass)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.BugReport, contentDescription = null, tint = VioletSoft)
                    Text(
                        text = "Test Monitorowania Błędów Sentry",
                        color = PearlWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Wygeneruj błąd i prześlij go natychmiast do panelu Sentry w celu diagnostyki i śledzenia błędów w czasie rzeczywistym.",
                    color = GrayText,
                    fontSize = 11.sp
                )

                TextField(
                    value = sentryMessage,
                    onValueChange = { sentryMessage = it },
                    placeholder = { Text("Wpisz treść błędu", color = GrayText) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkBgBlack,
                        unfocusedContainerColor = DarkBgBlack,
                        focusedTextColor = PearlWhite,
                        unfocusedTextColor = PearlWhite,
                        focusedIndicatorColor = VioletSoft,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                Button(
                    onClick = { onTriggerSampleException(sentryMessage) },
                    colors = ButtonDefaults.buttonColors(containerColor = VioletSoft),
                    shape = RoundedCornerShape(100.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                ) {
                    Text("Wyślij błąd do Sentry", color = DarkBgBlack, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
