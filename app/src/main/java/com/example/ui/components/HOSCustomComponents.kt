package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay
import kotlin.math.sin

// --- Somatic Silhouette Zone enum ---
enum class SomaticZone(val displayName: String, val description: String) {
    HEAD("Głowa (Umysł)", "Tłok myśli, napięcie mentalne, mgła mózgowa"),
    THROAT("Gardło (Ekspresja)", "Trudność w wypowiedzeniu się, ucisk, dławienie"),
    HEART("Serce (Emocje)", "Smutek, tęsknota, kłucie, ciężar, ucisk"),
    SOLAR_PLEXUS("Splot Słoneczny (Moc)", "Stres, lęk, ściskanie, spięcie, brak tchu"),
    GUT("Brzuch (Intuicja)", "Napięcie jelitowe, skurcz, niepokój visceralny")
}

@Composable
fun SomaticSilhouette(
    selectedZone: SomaticZone?,
    onZoneSelected: (SomaticZone) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(380.dp)
            .background(DarkBgMidnight)
    ) {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        // Determine which zone was tapped based on Y coordinate on the canvas
                        val yRatio = offset.y / height
                        val xRatio = offset.x / width
                        
                        // Only register taps near the center horizontally (where the body is drawn)
                        if (xRatio in 0.3f..0.7f) {
                            val zone = when {
                                yRatio < 0.22f -> SomaticZone.HEAD
                                yRatio < 0.35f -> SomaticZone.THROAT
                                yRatio < 0.52f -> SomaticZone.HEART
                                yRatio < 0.68f -> SomaticZone.SOLAR_PLEXUS
                                else -> SomaticZone.GUT
                            }
                            onZoneSelected(zone)
                        }
                    }
                }
        ) {
            val center = Offset(size.width / 2, size.height / 2)
            val headRadius = size.height * 0.08f
            val bodyWidth = size.width * 0.28f

            // 1. Draw glowing background aura
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        when (selectedZone) {
                            SomaticZone.HEAD -> VioletBrand.copy(alpha = 0.25f)
                            SomaticZone.HEART -> PinkRose.copy(alpha = 0.25f)
                            SomaticZone.SOLAR_PLEXUS -> GoldPremium.copy(alpha = 0.25f)
                            SomaticZone.GUT -> BlueSoft.copy(alpha = 0.25f)
                            else -> VioletBrand.copy(alpha = 0.1f)
                        },
                        Color.Transparent
                    ),
                    center = center,
                    radius = size.width * 0.45f
                )
            )

            // 2. Draw abstract body outline (Silhouette)
            // Draw head
            val headCenter = Offset(center.x, size.height * 0.16f)
            drawCircle(
                color = if (selectedZone == SomaticZone.HEAD) VioletBrand else PearlWhite.copy(alpha = 0.2f),
                radius = headRadius,
                center = headCenter,
                style = Stroke(width = 3.dp.toPx())
            )

            // Draw neck and shoulders
            val path = Path().apply {
                // Left neck
                moveTo(center.x - bodyWidth * 0.15f, headCenter.y + headRadius)
                lineTo(center.x - bodyWidth * 0.15f, headCenter.y + headRadius + 20f)
                // Left shoulder
                quadraticTo(
                    center.x - bodyWidth * 0.15f, headCenter.y + headRadius + 40f,
                    center.x - bodyWidth * 0.6f, headCenter.y + headRadius + 60f
                )
                // Left body side
                lineTo(center.x - bodyWidth * 0.4f, size.height * 0.85f)
                // Leg joints base
                lineTo(center.x + bodyWidth * 0.4f, size.height * 0.85f)
                // Right body side
                lineTo(center.x + bodyWidth * 0.6f, headCenter.y + headRadius + 60f)
                // Right shoulder
                quadraticTo(
                    center.x + bodyWidth * 0.15f, headCenter.y + headRadius + 40f,
                    center.x + bodyWidth * 0.15f, headCenter.y + headRadius + 20f
                )
                close()
            }
            drawPath(
                path = path,
                color = PearlWhite.copy(alpha = 0.15f),
                style = Stroke(width = 2.dp.toPx())
            )

            // 3. Draw energy centers (Chakras/Zones) as beautiful glowing circles
            val zonesCenters = listOf(
                Pair(SomaticZone.HEAD, Offset(center.x, size.height * 0.16f)),
                Pair(SomaticZone.THROAT, Offset(center.x, size.height * 0.29f)),
                Pair(SomaticZone.HEART, Offset(center.x, size.height * 0.44f)),
                Pair(SomaticZone.SOLAR_PLEXUS, Offset(center.x, size.height * 0.58f)),
                Pair(SomaticZone.GUT, Offset(center.x, size.height * 0.74f))
            )

            for ((zone, point) in zonesCenters) {
                val isActive = selectedZone == zone
                val zoneColor = when (zone) {
                    SomaticZone.HEAD -> VioletSoft
                    SomaticZone.THROAT -> BlueSoft
                    SomaticZone.HEART -> PinkRose
                    SomaticZone.SOLAR_PLEXUS -> GoldPremium
                    SomaticZone.GUT -> Color(0xFF4CAF50)
                }

                // Draw pulsing aura if active
                if (isActive) {
                    drawCircle(
                        color = zoneColor.copy(alpha = 0.35f),
                        radius = 28.dp.toPx(),
                        center = point
                    )
                }

                // Draw main energy node
                drawCircle(
                    color = if (isActive) zoneColor else zoneColor.copy(alpha = 0.4f),
                    radius = if (isActive) 12.dp.toPx() else 8.dp.toPx(),
                    center = point
                )
                
                // Outer ring
                drawCircle(
                    color = zoneColor.copy(alpha = 0.7f),
                    radius = if (isActive) 20.dp.toPx() else 14.dp.toPx(),
                    center = point,
                    style = Stroke(width = 1.5.dp.toPx())
                )
            }
        }
    }
}

// --- Breathing exercise visualizer ---

@Composable
fun BreathingVisualizer(
    isBreathing: Boolean,
    onSessionCompleted: (Int) -> Unit, // total seconds
    modifier: Modifier = Modifier
) {
    var phase by remember { mutableStateOf("Przygotowanie") }
    var scale by remember { mutableStateOf(1f) }
    var elapsedSeconds by remember { mutableStateOf(0) }
    var countdown by remember { mutableStateOf(4) }

    // Spring animations for visual transitions
    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessVeryLow
        ),
        label = "BreathingScale"
    )

    val animatedColor by animateColorAsState(
        targetValue = when (phase) {
            "Wdech (Inhale)" -> VioletBrand
            "Wstrzymaj (Hold)" -> GoldPremium
            "Wydech (Exhale)" -> BlueSoft
            "Pustka (Hold Empty)" -> DarkSurfaceCard
            else -> GrayText
        },
        animationSpec = tween(1500),
        label = "BreathingColor"
    )

    // Control breathing sequence loop
    LaunchedEffect(isBreathing) {
        if (isBreathing) {
            elapsedSeconds = 0
            while (true) {
                // Inhale: 4s
                phase = "Wdech (Inhale)"
                scale = 1.8f
                countdown = 4
                repeat(4) {
                    delay(1000)
                    elapsedSeconds++
                    countdown--
                }

                // Hold full: 4s
                phase = "Wstrzymaj (Hold)"
                scale = 1.8f
                countdown = 4
                repeat(4) {
                    delay(1000)
                    elapsedSeconds++
                    countdown--
                }

                // Exhale: 4s
                phase = "Wydech (Exhale)"
                scale = 1.0f
                countdown = 4
                repeat(4) {
                    delay(1000)
                    elapsedSeconds++
                    countdown--
                }

                // Hold empty: 4s
                phase = "Pustka (Hold Empty)"
                scale = 1.0f
                countdown = 4
                repeat(4) {
                    delay(1000)
                    elapsedSeconds++
                    countdown--
                }
            }
        } else {
            phase = "Rozpocznij"
            scale = 1.2f
            if (elapsedSeconds > 0) {
                onSessionCompleted(elapsedSeconds)
                elapsedSeconds = 0
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp),
        contentAlignment = Alignment.Center
    ) {
        // Outer pulsing energy waves
        Box(
            modifier = Modifier
                .size(130.dp * animatedScale)
                .clip(CircleShape)
                .background(animatedColor.copy(alpha = 0.08f))
        )
        Box(
            modifier = Modifier
                .size(90.dp * animatedScale)
                .clip(CircleShape)
                .background(animatedColor.copy(alpha = 0.15f))
        )

        // Main Breathing Center Circle
        Box(
            modifier = Modifier
                .size(60.dp * animatedScale)
                .clip(CircleShape)
                .background(
                    Brush.radialGradient(
                        colors = listOf(animatedColor, animatedColor.copy(alpha = 0.4f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isBreathing) {
                Text(
                    text = countdown.toString(),
                    color = PearlWhite,
                    fontSize = (22 * animatedScale).sp,
                    fontWeight = FontWeight.Bold
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Soma Harmony",
                    tint = GoldPremium,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        // Phase Text Prompt overlay
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(DarkSurfaceCard.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = phase,
                color = PearlWhite,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                letterSpacing = 1.sp
            )
        }
    }
}

// --- Soundwave visualizer ---

@Composable
fun StateWaveform(
    isPlaying: Boolean,
    color: Color = VioletBrand,
    modifier: Modifier = Modifier
) {
    val phaseState = remember { mutableStateOf(0f) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            while (true) {
                phaseState.value += 0.1f
                delay(30)
            }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        val width = size.width
        val height = size.height
        val barWidth = 4.dp.toPx()
        val space = 3.dp.toPx()
        val numBars = (width / (barWidth + space)).toInt()

        for (i in 0 until numBars) {
            val progress = i.toFloat() / numBars
            // Create double sine-wave modulation representing sound waves
            val animatedHeightMultiplier = if (isPlaying) {
                sin(progress * 15f + phaseState.value) * 0.4f +
                sin(progress * 8f - phaseState.value * 0.5f) * 0.3f + 0.3f
            } else {
                0.05f + sin(progress * Math.PI.toFloat()) * 0.05f
            }

            val barHeight = (height * Math.abs(animatedHeightMultiplier)).coerceIn(4f, height)
            val x = i * (barWidth + space)
            val y = (height - barHeight) / 2f

            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(color, color.copy(alpha = 0.3f))
                ),
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2, barWidth / 2)
            )
        }
    }
}

// --- Stress History Line Graph ---

@Composable
fun StressHistoryChart(
    stressScores: List<Int>, // list of integers (1 to 10)
    modifier: Modifier = Modifier
) {
    if (stressScores.isEmpty()) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
            border = BorderStroke(1.dp, BorderGlass)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Brak zarejestrowanych sesji. Rozpocznij trening.",
                    color = GrayText,
                    fontSize = 14.sp
                )
            }
        }
        return
    }

    // Fill minimum size for aesthetic line representation
    val displayScores = remember(stressScores) {
        if (stressScores.size == 1) listOf(stressScores[0], stressScores[0]) else stressScores.takeLast(7)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
        border = BorderStroke(1.dp, BorderGlass)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Hormonogram Stresu (Ostatnie 7 dni)",
                color = GrayText,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val width = size.width
                val height = size.height
                val pointCount = displayScores.size
                val stepX = width / (pointCount - 1)

                // Define coordinates for each score
                val points = displayScores.mapIndexed { idx, score ->
                    val normalizedY = (10f - score) / 10f // inverse Y since canvas 0,0 is top-left
                    Offset(idx * stepX, normalizedY * height)
                }

                // 1. Draw glowing background gradient under the curve
                val gradientPath = Path().apply {
                    moveTo(0f, height)
                    for (i in points.indices) {
                        lineTo(points[i].x, points[i].y)
                    }
                    lineTo(width, height)
                    close()
                }

                drawPath(
                    path = gradientPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(BlueSoft.copy(alpha = 0.3f), Color.Transparent)
                    )
                )

                // 2. Draw actual connecting line
                val linePath = Path().apply {
                    moveTo(points[0].x, points[0].y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }
                drawPath(
                    path = linePath,
                    color = BlueSoft,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )

                // 3. Draw dots representing score events
                for (point in points) {
                    drawCircle(
                        color = GoldPremium,
                        radius = 4.dp.toPx(),
                        center = point
                    )
                    drawCircle(
                        color = GoldPremium.copy(alpha = 0.3f),
                        radius = 8.dp.toPx(),
                        center = point,
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
            }
        }
    }
}
