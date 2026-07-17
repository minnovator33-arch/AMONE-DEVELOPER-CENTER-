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
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
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
import com.example.ui.components.StateWaveform
import com.example.ui.theme.*

data class SoundTrack(
    val title: String,
    val category: String,
    val duration: String,
    val description: String
)

@Composable
fun StateAlchemyScreen(
    generatedMusicStatus: String?,
    isGenerating: Boolean,
    onGenerateMusic: (String) -> Unit
) {
    var promptText by remember { mutableStateOf("Głęboki ambient tybetański z falami alfa 8Hz i misami śpiewającymi") }
    var isPlayingTrack by remember { mutableStateOf(false) }
    var selectedTrackTitle by remember { mutableStateOf("Coherence Alpha Gateway") }

    val presetTracks = remember {
        listOf(
            SoundTrack("Coherence Alpha Gateway", "ALPHA WAVE", "12:40", "Harmonizacja pracy serca i mózgu w częstotliwości 8Hz."),
            SoundTrack("Theta Subconscious Repatterning", "THETA SOMA", "20:00", "Przeprogramowanie podświadomych lęków w głębokim śnie."),
            SoundTrack("Solgexx 528Hz DNA Resonance", "SOLFEGGIO", "15:00", "Częstotliwość rezonansowa sprzyjająca komórkowej odnowie.")
        )
    }

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
                    text = "State Alchemy™",
                    color = GoldPremium,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Alchemia Stanów. Przetransformuj fale mózgowe i emocje poprzez dźwięk binarnego rezonansu.",
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
        }

        // --- 2. Sound player and Animated Waveform Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, GoldPremium.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "AKTYWNY REZONANS: $selectedTrackTitle",
                        color = GoldPremium,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Sound wave canvas visualizer
                    StateWaveform(
                        isPlaying = isPlayingTrack,
                        color = GoldPremium,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Player buttons
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        IconButton(
                            onClick = { isPlayingTrack = !isPlayingTrack },
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(GoldPremium)
                                .testTag("play_pause_alchemy")
                        ) {
                            Icon(
                                imageVector = if (isPlayingTrack) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Play/Pause",
                                tint = DarkBgBlack,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Generative Audio composer workspace (Lyria Model) ---
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
                        Icon(Icons.Default.MusicNote, contentDescription = null, tint = GoldPremium)
                        Text(
                            text = "Generatywny Kompozytor Lyria 3",
                            color = PearlWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Stwórz spersonalizowaną ścieżkę dźwiękową Box Breathing lub medytacji za pomocą modelu Lyria-3-pro-preview.",
                        color = GrayText,
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )

                    // Presets quick-pick row
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        PresetChip(text = "Sen") { promptText = "Misy kryształowe fali delta 4Hz ułatwiające zapadnięcie w sen" }
                        PresetChip(text = "Focus") { promptText = "Dźwięki binauralne gamma 40Hz stymulujące koncentrację" }
                        PresetChip(text = "Relaks") { promptText = "Ambient harfowy o częstotliwości uziemienia schumanna" }
                    }

                    TextField(
                        value = promptText,
                        onValueChange = { promptText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("alchemy_music_prompt"),
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

                    Button(
                        onClick = { onGenerateMusic(promptText) },
                        enabled = !isGenerating,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPremium),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("generate_music_button")
                    ) {
                        Text(
                            text = if (isGenerating) "Komponowanie dźwięku..." else "Skomponuj Utwór Lyria",
                            color = DarkBgBlack,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    if (isGenerating) {
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            color = GoldPremium,
                            trackColor = BorderGlass
                        )
                    }

                    generatedMusicStatus?.let { status ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(GoldPremium.copy(alpha = 0.08f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                                .clickable {
                                    selectedTrackTitle = "Generowany utwór Lyria"
                                    isPlayingTrack = true
                                }
                        ) {
                            Text(
                                text = "$status\n*(Kliknij, aby odtworzyć w odtwarzaczu)*",
                                color = PearlWhite,
                                fontSize = 12.sp,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 4. Library sound lists ---
        item {
            Text(
                text = "Zasoby Dźwiękowe Twojej Świadomości",
                color = PearlWhite,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(presetTracks) { track ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        selectedTrackTitle = track.title
                        isPlayingTrack = true
                    }
                    .testTag("track_${track.title.replace(" ", "_").lowercase()}"),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedTrackTitle == track.title) DarkSurfaceCard else DarkSurfaceCard.copy(alpha = 0.5f)
                ),
                border = BorderStroke(
                    1.dp,
                    if (selectedTrackTitle == track.title) GoldPremium.copy(alpha = 0.4f) else BorderGlass.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(
                                if (selectedTrackTitle == track.title) GoldPremium.copy(alpha = 0.15f) else BorderGlass
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = null,
                            tint = if (selectedTrackTitle == track.title) GoldPremium else GrayText
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = track.title,
                                color = PearlWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Box(
                                modifier = Modifier
                                    .background(GoldPremium.copy(alpha = 0.1f), RoundedCornerShape(100.dp))
                                    .padding(horizontal = 6.dp, vertical = 1.5.dp)
                            ) {
                                Text(
                                    text = track.category,
                                    color = GoldPremium,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Text(
                            text = track.description,
                            color = GrayText,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PresetChip(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(100.dp))
            .background(BorderGlass)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, color = PearlWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
    }
}
