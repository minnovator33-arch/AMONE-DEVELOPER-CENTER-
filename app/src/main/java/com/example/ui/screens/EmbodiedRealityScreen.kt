package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SomaticSilhouette
import com.example.ui.components.SomaticZone
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EmbodiedRealityScreen(
    currentSomaReading: String?,
    isGenerating: Boolean,
    onLogCheckIn: (emotion: String, intensity: Int, zone: SomaticZone, note: String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var selectedZone by remember { mutableStateOf<SomaticZone?>(SomaticZone.HEART) }
    var emotionText by remember { mutableStateOf("Niepokój") }
    var intensityValue by remember { mutableStateOf(5f) }
    var notesText by remember { mutableStateOf("Nacisk w klatce piersiowej przy głębokim wdechu.") }

    var imageAnalysisResult by remember { mutableStateOf<String?>(null) }
    var isAnalyzingImage by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack)
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Screen Header ---
        item {
            Column {
                Text(
                    text = "Embodied Reality™",
                    color = VioletSoft,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Świadomość somatyczna. Zlokalizuj emocje w ciele i odkoduj ich ukryty język.",
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
        }

        // --- 2. Somatic Silhouette Custom Canvas Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, BorderGlass)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Dotknij punktu na sylwetce ciala:",
                        color = GrayText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    SomaticSilhouette(
                        selectedZone = selectedZone,
                        onZoneSelected = { selectedZone = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                    )

                    selectedZone?.let { zone ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(VioletBrand.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = VioletSoft,
                                modifier = Modifier.size(20.dp)
                            )
                            Column {
                                Text(
                                    text = zone.displayName,
                                    color = PearlWhite,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = zone.description,
                                    color = GrayText,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Log Details Form Card ---
        selectedZone?.let { zone ->
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BorderGlass)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Opisz swój obecny stan",
                            color = PearlWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Emotion Text Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "Odczuwana Emocja", color = GrayText, fontSize = 12.sp)
                            TextField(
                                value = emotionText,
                                onValueChange = { emotionText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("soma_emotion_input"),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = DarkBgBlack,
                                    unfocusedContainerColor = DarkBgBlack,
                                    focusedTextColor = PearlWhite,
                                    unfocusedTextColor = PearlWhite,
                                    focusedIndicatorColor = VioletSoft,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Intensity Slider
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(text = "Intensywność odczucia", color = GrayText, fontSize = 12.sp)
                                Text(
                                    text = "${intensityValue.toInt()}/10",
                                    color = VioletSoft,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Slider(
                                value = intensityValue,
                                onValueChange = { intensityValue = it },
                                valueRange = 1f..10f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = VioletSoft,
                                    activeTrackColor = VioletSoft,
                                    inactiveTrackColor = BorderGlass
                                ),
                                modifier = Modifier.testTag("soma_intensity_slider")
                            )
                        }

                        // Sensory notes text field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "Notatki sensoryczne (Co dokładnie czujesz?)", color = GrayText, fontSize = 12.sp)
                            TextField(
                                value = notesText,
                                onValueChange = { notesText = it },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("soma_notes_input"),
                                minLines = 2,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = DarkBgBlack,
                                    unfocusedContainerColor = DarkBgBlack,
                                    focusedTextColor = PearlWhite,
                                    unfocusedTextColor = PearlWhite,
                                    focusedIndicatorColor = VioletSoft,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        Button(
                            onClick = {
                                onLogCheckIn(emotionText, intensityValue.toInt(), zone, notesText)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = VioletBrand),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("submit_soma_checkin")
                        ) {
                            Text(
                                text = "Rozpocznij Skanowanie Somatyczne",
                                color = PearlWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Live Soma Reading Report Card (Gemini Analysis Output) ---
        if (currentSomaReading != null) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, VioletSoft)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(VioletBrand.copy(alpha = 0.12f), Color.Transparent)
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SelfImprovement,
                                    contentDescription = null,
                                    tint = VioletSoft
                                )
                                Text(
                                    text = "Raport Somatyczny HOS",
                                    color = VioletSoft,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            HorizontalDivider(color = BorderGlass)

                            Text(
                                text = currentSomaReading,
                                color = PearlWhite,
                                fontSize = 13.sp,
                                lineHeight = 19.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 5. Multimodal Posture Upload & Analysis Card (Gemini Pro) ---
        item {
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
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = GoldPremium)
                        Text(
                            text = "Analiza Postawy / Mimiki (Multimodal Pro)",
                            color = PearlWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Udostępnij zdjęcie swojej ekspresji twarzy lub postawy ciała, aby Gemini Pro zanalizował mikromimikę i zablokowane napięcia.",
                        color = GrayText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Simulated Camera Trigger
                    Button(
                        onClick = {
                            isAnalyzingImage = true
                            imageAnalysisResult = null
                            // Simulate multimodal upload analysis by Gemini-3.1-pro-preview
                            scope.launch {
                                delay(2000)
                                imageAnalysisResult = """
                                    Wykryto asymetrię lewego barku (napięcie mięśnia czworobocznego) oraz mikroskurcz wokół mięśni żwaczy (klasyczny somatyczny objaw ukrywanego stresu/tłumionej ekspresji). 
                                    Wskazówka Pro: Wykonaj 5 powtórzeń swobodnego opuszczania żuchwy z jednoczesnym głośnym westchnięciem 'Aaaah', aby odciążyć nerw twarzowy.
                                """.trimIndent()
                                isAnalyzingImage = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = DarkBgBlack),
                        border = BorderStroke(1.dp, GoldPremium),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("posture_analysis_button")
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = GoldPremium)
                            Text(text = "Zrób / Załaduj Zdjęcie", color = GoldPremium, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isAnalyzingImage) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 1.5.dp, color = GoldPremium)
                            Text(text = "Uruchamianie Gemini Pro Image Understanding...", color = GoldPremium, fontSize = 11.sp)
                        }
                    }

                    imageAnalysisResult?.let { result ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                                .background(GoldPremium.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = result,
                                color = PearlWhite,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
