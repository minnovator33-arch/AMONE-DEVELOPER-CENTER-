package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.database.ChatMessageEntity
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    messages: List<ChatMessageEntity>,
    isGenerating: Boolean,
    useThinking: Boolean,
    useSearch: Boolean,
    useMaps: Boolean,
    ttsEnabled: Boolean,
    lowLatencyEnabled: Boolean,
    apiError: String?,
    onSendMessage: (String) -> Unit,
    onToggleThinking: (Boolean) -> Unit,
    onToggleSearch: (Boolean) -> Unit,
    onToggleMaps: (Boolean) -> Unit,
    onToggleTts: (Boolean) -> Unit,
    onToggleLowLatency: (Boolean) -> Unit,
    onClearHistory: () -> Unit
) {
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Scroll to bottom when new messages arrive
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            scope.launch {
                listState.animateScrollToItem(messages.size - 1)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack)
    ) {
        // --- 1. Top Bar Controls ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkBgMidnight)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "AMONE Guide AI",
                    color = PearlWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inteligentny Asystent Somatyczny",
                    color = GrayText,
                    fontSize = 11.sp
                )
            }

            IconButton(
                onClick = onClearHistory,
                modifier = Modifier.testTag("clear_history_button")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Clear Chat",
                    tint = GrayText
                )
            }
        }

        // --- 2. Advanced Feature Toggles row ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurfaceCard)
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // High Thinking Toggle
                ToggleChip(
                    text = "Myślenie (High)",
                    selected = useThinking,
                    activeColor = VioletSoft,
                    icon = Icons.Default.Psychology,
                    onSelectedChange = onToggleThinking,
                    modifier = Modifier.testTag("toggle_thinking")
                )

                // Search Grounding Toggle
                ToggleChip(
                    text = "Google Search",
                    selected = useSearch,
                    activeColor = BlueSoft,
                    icon = Icons.Default.Search,
                    onSelectedChange = onToggleSearch,
                    modifier = Modifier.testTag("toggle_search")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Maps Grounding Toggle
                ToggleChip(
                    text = "Google Maps",
                    selected = useMaps,
                    activeColor = GoldPremium,
                    icon = Icons.Default.Place,
                    onSelectedChange = onToggleMaps,
                    modifier = Modifier.testTag("toggle_maps")
                )

                // Speech Output (TTS) Toggle
                ToggleChip(
                    text = "Dźwięk (TTS)",
                    selected = ttsEnabled,
                    activeColor = PinkRose,
                    icon = Icons.Default.VolumeUp,
                    onSelectedChange = onToggleTts,
                    modifier = Modifier.testTag("toggle_tts")
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Low Latency Toggle
                ToggleChip(
                    text = "Niska Latencja",
                    selected = lowLatencyEnabled,
                    activeColor = Color(0xFF4CAF50),
                    icon = Icons.Default.Bolt,
                    onSelectedChange = onToggleLowLatency,
                    modifier = Modifier.testTag("toggle_low_latency")
                )
            }
        }

        // --- 3. Error Banner (if any) ---
        if (apiError != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF5C1E26))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = apiError,
                    color = PearlWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // --- 4. Chat Thread ---
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(VioletBrand.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Forum,
                                    contentDescription = null,
                                    tint = VioletSoft,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Rozpocznij konwersację z AMONE Guide",
                                color = PearlWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Zadaj pytanie dotyczące regulacji lęku, emocji w ciele, faz cyklu lub poproś o analizę Twoich wzorców.",
                                color = GrayText,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 24.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            items(messages) { message ->
                ChatBubble(message)
            }

            if (isGenerating) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard.copy(alpha = 0.6f)),
                            border = BorderStroke(1.dp, BorderGlass)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(14.dp),
                                    strokeWidth = 1.5.dp,
                                    color = VioletSoft
                                )
                                Text(
                                    text = if (useThinking) "Myślenie modelu Gemini..." else "Komponowanie odpowiedzi...",
                                    color = VioletSoft,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 5. Message Input & Transcription Simulator ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = DarkBgMidnight,
            border = BorderStroke(1.dp, BorderGlass)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Mic Button (Transcribe simulation)
                IconButton(
                    onClick = {
                        inputText = "Czuję silne spięcie w klatce piersiowej i trudność ze złapaniem oddechu."
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BorderGlass)
                        .testTag("mic_transcribe_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Transcribe Audio",
                        tint = GoldPremium
                    )
                }

                TextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Zadaj pytanie...", color = GrayText, fontSize = 14.sp) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("chat_text_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = DarkSurfaceCard,
                        unfocusedContainerColor = DarkSurfaceCard,
                        focusedTextColor = PearlWhite,
                        unfocusedTextColor = PearlWhite,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                // Send Button
                IconButton(
                    onClick = {
                        if (inputText.trim().isNotEmpty()) {
                            onSendMessage(inputText)
                            inputText = ""
                        }
                    },
                    enabled = inputText.trim().isNotEmpty() && !isGenerating,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(if (inputText.trim().isNotEmpty()) GoldPremium else BorderGlass)
                        .testTag("chat_send_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send Message",
                        tint = if (inputText.trim().isNotEmpty()) DarkBgBlack else GrayText
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessageEntity) {
    val isUser = message.sender == "USER"
    val alignment = if (isUser) Alignment.End else Alignment.Start
    val bubbleColor = if (isUser) DarkSurfaceCard else VioletBrand.copy(alpha = 0.12f)
    val borderColor = if (isUser) BorderGlass else VioletBrand.copy(alpha = 0.35f)
    val textColor = if (isUser) PearlWhite else PearlWhite

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = alignment
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = bubbleColor),
            border = BorderStroke(1.dp, borderColor),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 290.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Header badge tags for features used
                if (!isUser && (message.isThinking || message.isSearch || message.isMaps)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        if (message.isThinking) {
                            FeatureBadge(text = "High Thinking", color = VioletSoft)
                        }
                        if (message.isSearch) {
                            FeatureBadge(text = "Search Grounded", color = BlueSoft)
                        }
                        if (message.isMaps) {
                            FeatureBadge(text = "Maps Grounded", color = GoldPremium)
                        }
                    }
                }

                Text(
                    text = message.text,
                    color = textColor,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun FeatureBadge(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(100.dp))
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(text = text, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ToggleChip(
    text: String,
    selected: Boolean,
    activeColor: Color,
    icon: ImageVector,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(if (selected) activeColor.copy(alpha = 0.15f) else BorderGlass)
            .clickable { onSelectedChange(!selected) }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) activeColor else GrayText,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = text,
                color = if (selected) PearlWhite else GrayText,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
