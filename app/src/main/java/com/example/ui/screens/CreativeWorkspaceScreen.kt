package com.example.ui.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun CreativeWorkspaceScreen(
    generatedImageB64: String?,
    generatedVideoStatus: String?,
    isGenerating: Boolean,
    onGenerateImage: (prompt: String, size: String, aspectRatio: String) -> Unit,
    onGenerateVideo: (prompt: String, aspectRatio: String) -> Unit
) {
    var activeTab by remember { mutableStateOf("IMAGE") } // "IMAGE" or "VIDEO"

    // Safe base64 image decoding outside direct Composable drawing scope
    val decodedBitmap = remember(generatedImageB64) {
        if (generatedImageB64 != null && generatedImageB64 != "SIMULATED_ART") {
            try {
                val imageBytes = Base64.decode(generatedImageB64, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
            } catch (e: Exception) {
                null
            }
        } else {
            null
        }
    }

    // Image states
    var imagePrompt by remember { mutableStateOf("Kobiecy awatar w kosmicznym hełmie ze złotym światłem zorzy polarnej, styl cyberpunk") }
    var selectedRatio by remember { mutableStateOf("1:1") }
    var selectedSize by remember { mutableStateOf("1K") }

    // Video states
    var videoPrompt by remember { mutableStateOf("Krople rosy na płatku lotosu, zwolnione tempo 120fps, głębokie rozmycie tła") }
    var selectedVideoAspect by remember { mutableStateOf("16:9") }

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
                    text = "Studio Kreatywne AI",
                    color = VioletSoft,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Wygeneruj wysokiej jakości zasoby graficzne oraz wideo Veo 3.",
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
        }

        // --- 2. Workspace Selector Tabs ---
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(100.dp))
                    .background(DarkSurfaceCard)
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (activeTab == "IMAGE") VioletBrand else Color.Transparent)
                        .clickable { activeTab = "IMAGE" }
                        .padding(vertical = 10.dp)
                        .testTag("tab_image_generation"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Grafika (Pro Image)",
                        color = if (activeTab == "IMAGE") PearlWhite else GrayText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(100.dp))
                        .background(if (activeTab == "VIDEO") VioletBrand else Color.Transparent)
                        .clickable { activeTab = "VIDEO" }
                        .padding(vertical = 10.dp)
                        .testTag("tab_video_generation"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Wideo (Veo 3.1)",
                        color = if (activeTab == "VIDEO") PearlWhite else GrayText,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- 3. Image Generation Segment ---
        if (activeTab == "IMAGE") {
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
                            text = "Parametry Grafiki",
                            color = PearlWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Prompt Text Field
                        TextField(
                            value = imagePrompt,
                            onValueChange = { imagePrompt = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("image_prompt_input"),
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

                        // Aspect Ratio Toggles
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "Proporcje Obrazu (Aspect Ratio)", color = GrayText, fontSize = 12.sp)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                listOf("1:1", "2:3", "3:2", "3:4", "4:3", "9:16", "16:9", "21:9").forEach { ratio ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (selectedRatio == ratio) VioletSoft.copy(alpha = 0.2f) else BorderGlass)
                                            .clickable { selectedRatio = ratio }
                                            .padding(vertical = 6.dp)
                                            .testTag("ratio_btn_$ratio"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = ratio,
                                            color = if (selectedRatio == ratio) VioletSoft else GrayText,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        // Image Resolution Selector (Specify 1K, 2K, 4K)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "Rozdzielczość Studio", color = GrayText, fontSize = 12.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("1K", "2K", "4K").forEach { size ->
                                    Box(
                                        modifier = Modifier
                                            .width(70.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (selectedSize == size) GoldPremium.copy(alpha = 0.2f) else BorderGlass)
                                            .clickable { selectedSize = size }
                                            .padding(vertical = 8.dp)
                                            .testTag("size_btn_$size"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = size,
                                            color = if (selectedSize == size) GoldPremium else GrayText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { onGenerateImage(imagePrompt, selectedSize, selectedRatio) },
                            enabled = !isGenerating,
                            colors = ButtonDefaults.buttonColors(containerColor = VioletBrand),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("submit_image_generation")
                        ) {
                            Text(
                                text = if (isGenerating) "Malowanie obrazu..." else "Wygeneruj Obraz 1K/2K/4K",
                                color = PearlWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Image generation display result
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BorderGlass)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isGenerating) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = VioletSoft)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "Zgłaszanie żądania do Gemini Pro Image...", color = VioletSoft, fontSize = 12.sp)
                            }
                        } else if (generatedImageB64 != null) {
                            if (generatedImageB64 == "SIMULATED_ART") {
                                // Draw beautiful abstract gradient card representing simulated generated artwork
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(VioletBrand, BlueSoft, GoldPremium)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PearlWhite, modifier = Modifier.size(44.dp))
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(text = "Wygenerowana Grafika $selectedSize", color = PearlWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(text = "Proporcje: $selectedRatio", color = PearlWhite.copy(alpha = 0.8f), fontSize = 12.sp)
                                    }
                                }
                            } else {
                                // Decode real base64 image if present
                                if (decodedBitmap != null) {
                                    Image(
                                        bitmap = decodedBitmap.asImageBitmap(),
                                        contentDescription = "Generated artwork",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Text(text = "Błąd dekodowania obrazu.", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = BorderGlass, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Brak wygenerowanego obrazu.", color = GrayText, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Veo Video Generation Segment ---
        if (activeTab == "VIDEO") {
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
                            text = "Parametry Modelu Veo 3.1",
                            color = PearlWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        // Prompt Text Field
                        TextField(
                            value = videoPrompt,
                            onValueChange = { videoPrompt = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("video_prompt_input"),
                            minLines = 2,
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

                        // Video Aspect ratios (Mandatory 16:9 or 9:16)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(text = "Proporcje Klatki Veo (Landscape / Portrait)", color = GrayText, fontSize = 12.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                listOf("16:9", "9:16").forEach { aspect ->
                                    Box(
                                        modifier = Modifier
                                            .width(100.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (selectedVideoAspect == aspect) GoldPremium.copy(alpha = 0.2f) else BorderGlass)
                                            .clickable { selectedVideoAspect = aspect }
                                            .padding(vertical = 10.dp)
                                            .testTag("video_aspect_btn_$aspect"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = if (aspect == "16:9") "16:9 (Poziom)" else "9:16 (Pion)",
                                            color = if (selectedVideoAspect == aspect) GoldPremium else GrayText,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = { onGenerateVideo(videoPrompt, selectedVideoAspect) },
                            enabled = !isGenerating,
                            colors = ButtonDefaults.buttonColors(containerColor = GoldPremium),
                            shape = RoundedCornerShape(100.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("submit_video_generation")
                        ) {
                            Text(
                                text = if (isGenerating) "Inicjowanie klatek Veo..." else "Wygeneruj Wideo Veo 3.1",
                                color = DarkBgBlack,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // Video player simulation result
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                    border = BorderStroke(1.dp, BorderGlass)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isGenerating) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = GoldPremium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(text = "Rendrowanie klatek w chmurze Veo...", color = GoldPremium, fontSize = 12.sp)
                            }
                        } else if (generatedVideoStatus != null) {
                            // Animated simulated video background loop
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(DarkBgMidnight, BlueSoft.copy(alpha = 0.4f))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(16.dp)) {
                                    Icon(Icons.Default.Movie, contentDescription = null, tint = GoldPremium, modifier = Modifier.size(44.dp))
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = generatedVideoStatus,
                                        color = PearlWhite,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 12.sp,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Movie, contentDescription = null, tint = BorderGlass, modifier = Modifier.size(48.dp))
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Brak wygenerowanego wideo Veo.", color = GrayText, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
