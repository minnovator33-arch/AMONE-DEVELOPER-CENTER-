package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.CheckInEntity
import com.example.ui.theme.*

@Composable
fun DashboardScreen(
    checkIns: List<CheckInEntity>,
    userEmail: String?,
    isSyncing: Boolean,
    onNavigateTo: (String) -> Unit,
    onSync: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Atmospheric Hero Brand Header ---
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(VioletBrand.copy(alpha = 0.35f), BlueSoft.copy(alpha = 0.15f))
                        )
                    )
                    .padding(20.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .clip(CircleShape)
                                .background(GoldPremium)
                        )
                        Text(
                            text = "AMONE ECOSYSTEM V4",
                            color = GoldPremium,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Human Operating System",
                        color = PearlWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Digital technologies that reconnect you with yourself.",
                        color = GrayText,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // --- 2. Live Integration Status & Sync Banner ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, BorderGlass)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (userEmail != null) GoldPremium.copy(alpha = 0.15f) else BorderGlass),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (userEmail != null) Icons.Default.CloudDone else Icons.Default.CloudOff,
                                contentDescription = "Sync",
                                tint = if (userEmail != null) GoldPremium else GrayText
                            )
                        }
                        Column {
                            Text(
                                text = if (userEmail != null) "Połączono z Firebase" else "Tryb lokalny (Offline)",
                                color = PearlWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = userEmail ?: "Dane zapisywane w bezpiecznym Room DB",
                                color = GrayText,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (userEmail != null) {
                        Button(
                            onClick = onSync,
                            enabled = !isSyncing,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPremium),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier.testTag("sync_data_button")
                        ) {
                            Text(
                                text = if (isSyncing) "Synchronizacja..." else "Zsynchronizuj",
                                color = DarkBgBlack,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = { onNavigateTo("auth") },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkSurfaceCard),
                            border = BorderStroke(1.dp, GoldPremium),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier.testTag("login_redirect_button")
                        ) {
                            Text(
                                text = "Zaloguj",
                                color = GoldPremium,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Primary Core Sub-systems (Cards Grid) ---
        item {
            Text(
                text = "Główne Moduły Systemowe",
                color = PearlWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // 1. Embodied Reality
                ModuleCard(
                    title = "Embodied Reality™",
                    subtitle = "Skanowanie somatyczne, emocje i tożsamość",
                    accentColor = VioletBrand,
                    icon = Icons.Default.AccessibilityNew,
                    onClick = { onNavigateTo("embodied") }
                )

                // 2. Regulation System
                ModuleCard(
                    title = "Regulation System™",
                    subtitle = "Biorytm stresu i trening oddechowy Box Breathing",
                    accentColor = BlueSoft,
                    icon = Icons.Default.Spa,
                    onClick = { onNavigateTo("regulation") }
                )

                // 3. State Alchemy
                ModuleCard(
                    title = "State Alchemy™",
                    subtitle = "Generowanie muzyki Lyria i fale dudnień różnicowych",
                    accentColor = GoldPremium,
                    icon = Icons.Default.MusicNote,
                    onClick = { onNavigateTo("alchemy") }
                )

                // 4. HerAURA
                ModuleCard(
                    title = "HerAURA™",
                    subtitle = "Śledzenie faz cyklu, bioenergii i nastroju",
                    accentColor = PinkRose,
                    icon = Icons.Default.BubbleChart,
                    onClick = { onNavigateTo("heraura") }
                )
            }
        }

        // --- 4. Extra AI Workspaces Navigation Section ---
        item {
            Text(
                text = "Zasoby Kreatywne AI",
                color = PearlWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTo("creative") }
                        .testTag("creative_button"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BorderGlass)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(VioletSoft.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = VioletSoft)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Kreacja AI",
                            color = PearlWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Generuj obrazy i wideo Veo",
                            color = GrayText,
                            fontSize = 11.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateTo("chat") }
                        .testTag("chat_button"),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BorderGlass)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(GoldPremium.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null, tint = GoldPremium)
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Asystent HOS",
                            color = PearlWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Czat, wyszukiwanie i myślenie",
                            color = GrayText,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // --- 5. Historic Log list previews ---
        if (checkIns.isNotEmpty()) {
            item {
                Text(
                    text = "Ostatnie Logi Samopoczucia",
                    color = PearlWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
            }

            items(checkIns.take(3)) { checkIn ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard.copy(alpha = 0.5f)),
                    border = BorderStroke(1.dp, BorderGlass.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "${checkIn.emotion} (${checkIn.somaticArea})",
                                color = PearlWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            if (checkIn.note.isNotEmpty()) {
                                Text(
                                    text = checkIn.note,
                                    color = GrayText,
                                    fontSize = 12.sp,
                                    maxLines = 1
                                )
                            }
                        }
                        Text(
                            text = "Intensywność: ${checkIn.intensity}/10",
                            color = GoldPremium,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ModuleCard(
    title: String,
    subtitle: String,
    accentColor: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag(title.replace("™", "").replace(" ", "_").lowercase() + "_card"),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, BorderGlass)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = PearlWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = GrayText,
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = GrayText.copy(alpha = 0.6f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
