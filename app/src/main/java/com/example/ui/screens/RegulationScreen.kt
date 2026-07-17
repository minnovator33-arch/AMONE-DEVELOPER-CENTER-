package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.BreathSessionEntity
import com.example.ui.components.BreathingVisualizer
import com.example.ui.components.StressHistoryChart
import com.example.ui.theme.*

@Composable
fun RegulationScreen(
    breathSessions: List<BreathSessionEntity>,
    onLogBreathSession: (durationSeconds: Int, startStress: Int, endStress: Int) -> Unit
) {
    var isBreathingActive by remember { mutableStateOf(false) }
    var preStressScore by remember { mutableStateOf(6f) }
    var postStressScore by remember { mutableStateOf(3f) }
    var lastLoggedSeconds by remember { mutableStateOf(0) }
    var showLoggingForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Header Section ---
        item {
            Column {
                Text(
                    text = "Regulation System™",
                    color = BlueSoft,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Układ nerwowy. Przejdź z trybu przetrwania (survival) do koherencji serca.",
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
        }

        // --- 2. Interactive Animated Breathing Box Visualizer ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, BorderGlass)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Trening Oddechowy Box Breathing (4-4-4-4)",
                        color = GrayText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    BreathingVisualizer(
                        isBreathing = isBreathingActive,
                        onSessionCompleted = { seconds ->
                            lastLoggedSeconds = seconds
                            showLoggingForm = true
                            isBreathingActive = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { isBreathingActive = !isBreathingActive },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBreathingActive) MaterialTheme.colorScheme.error else BlueSoft
                        ),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(48.dp)
                            .testTag("toggle_breathing_session")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isBreathingActive) Icons.Default.Stop else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = PearlWhite
                            )
                            Text(
                                text = if (isBreathingActive) "Zatrzymaj Sesję" else "Rozpocznij Box Breathing",
                                color = PearlWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Dynamic Stress Logging dialog form ---
        if (showLoggingForm) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BlueSoft)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Sesja zakończona! Zaloguj postępy stresu",
                            color = BlueSoft,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Czas trwania: ${lastLoggedSeconds} sekund. Jak zmieniło się Twoje napięcie?",
                            color = GrayText,
                            fontSize = 12.sp
                        )

                        // Pre stress score slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Stres PRZED sesją", color = GrayText, fontSize = 12.sp)
                                Text(
                                    text = "${preStressScore.toInt()}/10",
                                    color = PearlWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Slider(
                                value = preStressScore,
                                onValueChange = { preStressScore = it },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = BlueSoft,
                                    activeTrackColor = BlueSoft,
                                    inactiveTrackColor = BorderGlass
                                ),
                                modifier = Modifier.testTag("pre_stress_slider")
                            )
                        }

                        // Post stress score slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Stres PO sesji", color = GrayText, fontSize = 12.sp)
                                Text(
                                    text = "${postStressScore.toInt()}/10",
                                    color = BlueSoft,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Slider(
                                value = postStressScore,
                                onValueChange = { postStressScore = it },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = BlueSoft,
                                    activeTrackColor = BlueSoft,
                                    inactiveTrackColor = BorderGlass
                                ),
                                modifier = Modifier.testTag("post_stress_slider")
                            )
                        }

                        Button(
                            onClick = {
                                onLogBreathSession(lastLoggedSeconds, preStressScore.toInt(), postStressScore.toInt())
                                showLoggingForm = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = BlueSoft),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("save_stress_log_button")
                        ) {
                            Text(
                                text = "Zapisz do Zeszytu",
                                color = PearlWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Custom drawn Stress History Line Chart ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Twoja Historia Koherencji",
                    color = PearlWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                // Map historical sessions to stress level lists
                val historicalScores = breathSessions.map { it.endStress }.reversed()
                StressHistoryChart(
                    stressScores = if (historicalScores.isEmpty()) listOf(6, 4, 7, 5, 3) else historicalScores,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
