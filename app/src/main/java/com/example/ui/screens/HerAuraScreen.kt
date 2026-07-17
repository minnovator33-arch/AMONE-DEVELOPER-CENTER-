package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BubbleChart
import androidx.compose.material.icons.filled.Info
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
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun HerAuraScreen(
    onLogCycle: (phase: String, day: Int, physical: Int, emotional: Int) -> Unit
) {
    var selectedPhase by remember { mutableStateOf("Follicular") }
    var cycleDayValue by remember { mutableStateOf(8f) }
    var physicalEnergyValue by remember { mutableStateOf(7f) }
    var emotionalEnergyValue by remember { mutableStateOf(6f) }

    var hormonalCoachingResult by remember { mutableStateOf<String?>(null) }
    var isGeneratingCoaching by remember { mutableStateOf(false) }

    // Dynamic advice map
    val phaseDescriptions = remember {
        mapOf(
            "Menstrual" to "Faza oczyszczenia. Estrogeny i progesteron są na najniższym poziomie. Ciało oszczędza energię. Idealny czas na odpoczynek i autorefleksję.",
            "Follicular" to "Faza dynamiczna. Estrogeny rosną, stymulując wzrost pęcherzyków. Rośnie poziom energii, motywacji, optymizmu oraz kognitywnej zwinności.",
            "Ovulatory" to "Faza magnetyczna. Pik estrogenu i lh powoduje uwolnienie komórki jajowej. Najwyższa elokwencja, zdolności komunikacyjne i charyzma.",
            "Luteal" to "Faza uziemienia. Progesteron staje się dominującym hormonem. Ciało zwalnia tempo. Mogą pojawiać się objawy pms, czas na uspokojenie systemu."
        )
    }

    // Trigger advice once when phase changes
    LaunchedEffect(selectedPhase) {
        isGeneratingCoaching = true
        hormonalCoachingResult = null
        delay(1200)
        hormonalCoachingResult = when (selectedPhase) {
            "Menstrual" -> "Dieta: Rozgrzewające zupy, napary z pokrzywy (żelazo). Ruch: Rozciąganie, łagodny Yin jogi. Produktywność: Unikaj trudnych spotkań, przeznacz ten czas na podsumowania."
            "Follicular" -> "Dieta: Świeże warzywa, sfermentowane produkty. Ruch: Treningi siłowe, HIIT. Produktywność: Idealny czas na planowanie burz mózgów i naukę nowych języków."
            "Ovulatory" -> "Dieta: Lekkie sałatki, owoce, nawodnienie. Ruch: Aktywności towarzyskie, cardio, bieganie. Produktywność: Prezentacje, negocjacje, wystąpienia publiczne."
            else -> "Dieta: Kasze, zdrowe tłuszcze (awokado), orzechy (magnez). Ruch: Pilates, spacery leśne (shinrin-yoku). Produktywność: Prace administracyjne, porządkowanie dokumentów."
        }
        isGeneratingCoaching = false
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
                    text = "HerAURA™",
                    color = PinkRose,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Inteligencja Żeńska. Zrozum fazy biochemiczne, rytmy energetyczne i emocjonalne.",
                    color = GrayText,
                    fontSize = 13.sp
                )
            }
        }

        // --- 2. Interactive phase selector wheels ---
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
                    Text(
                        text = "Wybierz obecną Fazę Cyklu",
                        color = GrayText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Menstrual", "Follicular", "Ovulatory", "Luteal").forEach { phase ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(100.dp))
                                    .background(if (selectedPhase == phase) PinkRose.copy(alpha = 0.15f) else BorderGlass)
                                    .clickable { selectedPhase = phase }
                                    .padding(vertical = 10.dp)
                                    .testTag("phase_btn_$phase"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (phase) {
                                        "Menstrual" -> "Miesiączkowa"
                                        "Follicular" -> "Folikularna"
                                        "Ovulatory" -> "Owulacyjna"
                                        else -> "Lutealna"
                                    },
                                    color = if (selectedPhase == phase) PinkRose else GrayText,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Display active phase description
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(PinkRose.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = PinkRose)
                        Column {
                            Text(
                                text = "Charakterystyka Biologiczna:",
                                color = PearlWhite,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = phaseDescriptions[selectedPhase] ?: "",
                                color = GrayText,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }
        }

        // --- 3. Dynamic hormonal coaching advice Card (Gemini) ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkSurfaceCard),
                border = BorderStroke(1.dp, PinkRose.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.BubbleChart, contentDescription = null, tint = PinkRose)
                        Text(
                            text = "Hormonogram Wspierający HerAURA (Gemini)",
                            color = PinkRose,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(color = BorderGlass)

                    if (isGeneratingCoaching) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 1.5.dp, color = PinkRose)
                            Text(text = "Gemini pobiera spersonalizowane wskazówki...", color = PinkRose, fontSize = 11.sp)
                        }
                    }

                    hormonalCoachingResult?.let { result ->
                        Text(
                            text = result,
                            color = PearlWhite,
                            fontSize = 13.sp,
                            lineHeight = 19.sp
                        )
                    }
                }
            }
        }

        // --- 4. Bio-energy Logger Form Card ---
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
                        text = "Zaloguj dzisiejsze Bio-Energia",
                        color = PearlWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    // Cycle Day
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Dzień cyklu", color = GrayText, fontSize = 12.sp)
                            Text(
                                text = "${cycleDayValue.toInt()} dzień",
                                color = PinkRose,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Slider(
                            value = cycleDayValue,
                            onValueChange = { cycleDayValue = it },
                            valueRange = 1f..31f,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkRose,
                                activeTrackColor = PinkRose,
                                inactiveTrackColor = BorderGlass
                            ),
                            modifier = Modifier.testTag("cycle_day_slider")
                        )
                    }

                    // Physical Energy
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Witalność fizyczna", color = GrayText, fontSize = 12.sp)
                            Text(
                                text = "${physicalEnergyValue.toInt()}/10",
                                color = PinkRose,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Slider(
                            value = physicalEnergyValue,
                            onValueChange = { physicalEnergyValue = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkRose,
                                activeTrackColor = PinkRose,
                                inactiveTrackColor = BorderGlass
                            ),
                            modifier = Modifier.testTag("cycle_physical_slider")
                        )
                    }

                    // Emotional Energy
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Równowaga emocjonalna", color = GrayText, fontSize = 12.sp)
                            Text(
                                text = "${emotionalEnergyValue.toInt()}/10",
                                color = PinkRose,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        Slider(
                            value = emotionalEnergyValue,
                            onValueChange = { emotionalEnergyValue = it },
                            valueRange = 1f..10f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = PinkRose,
                                activeTrackColor = PinkRose,
                                inactiveTrackColor = BorderGlass
                            ),
                            modifier = Modifier.testTag("cycle_emotional_slider")
                        )
                    }

                    Button(
                        onClick = {
                            onLogCycle(selectedPhase, cycleDayValue.toInt(), physicalEnergyValue.toInt(), emotionalEnergyValue.toInt())
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PinkRose),
                        shape = RoundedCornerShape(100.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("save_cycle_log_btn")
                    ) {
                        Text(
                            text = "Zapisz Dzień w HerAURA",
                            color = PearlWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
