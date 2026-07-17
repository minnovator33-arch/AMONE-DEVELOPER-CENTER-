package com.example

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.data.database.HOSDatabase
import com.example.data.database.HOSRepository
import com.example.ui.screens.*
import com.example.ui.theme.DarkBgBlack
import com.example.ui.theme.GoldPremium
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.HOSViewModel
import com.example.ui.viewmodel.HOSViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                
                // Initialize Database and Repository
                val database = remember { HOSDatabase.getDatabase(context) }
                val repository = remember { HOSRepository(database.hosDao()) }
                
                // Construct ViewModel with simple factory constructor injection
                val viewModel: HOSViewModel = viewModel(
                    factory = HOSViewModelFactory(
                        context.applicationContext as Application,
                        repository
                    )
                )

                AMONEHOSApp(viewModel)
            }
        }
    }
}

// --- Navigation Item Spec ---
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val tag: String
) {
    object Dashboard : BottomNavItem("dashboard", "HOS Hub", Icons.Filled.Home, Icons.Outlined.Home, "nav_dashboard")
    object Chat : BottomNavItem("chat", "Guide AI", Icons.Filled.Chat, Icons.Outlined.Chat, "nav_chat")
    object Creative : BottomNavItem("creative", "Kreacja", Icons.Filled.Palette, Icons.Outlined.Palette, "nav_creative")
    object Auth : BottomNavItem("auth", "Konto", Icons.Filled.CloudDone, Icons.Outlined.CloudQueue, "nav_auth")
}

@Composable
fun AMONEHOSApp(viewModel: HOSViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Collect Room database flows reactively
    val checkIns by viewModel.checkIns.collectAsStateWithLifecycle()
    val breathSessions by viewModel.breathSessions.collectAsStateWithLifecycle()
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()

    // Collect interactive states
    val isGenerating by viewModel.isGenerating.collectAsStateWithLifecycle()
    val useThinking by viewModel.useThinking.collectAsStateWithLifecycle()
    val useSearch by viewModel.useSearch.collectAsStateWithLifecycle()
    val useMaps by viewModel.useMaps.collectAsStateWithLifecycle()
    val ttsEnabled by viewModel.ttsEnabled.collectAsStateWithLifecycle()
    val lowLatencyEnabled by viewModel.lowLatencyEnabled.collectAsStateWithLifecycle()

    // Collect generations output
    val generatedImageB64 by viewModel.generatedImageB64.collectAsStateWithLifecycle()
    val generatedVideoStatus by viewModel.generatedVideoStatus.collectAsStateWithLifecycle()
    val generatedMusicStatus by viewModel.generatedMusicStatus.collectAsStateWithLifecycle()
    val apiError by viewModel.apiErrorMessage.collectAsStateWithLifecycle()

    // Collect Firebase states
    val userEmail by viewModel.userEmail.collectAsStateWithLifecycle()
    val isSyncing by viewModel.isSyncing.collectAsStateWithLifecycle()
    val isAuthLoading by viewModel.isAuthLoading.collectAsStateWithLifecycle()
    val authErrorMessage by viewModel.authErrorMessage.collectAsStateWithLifecycle()

    // Collect Somatic status
    val currentSomaReading by viewModel.currentSomaReading.collectAsStateWithLifecycle()

    val navItems = listOf(
        BottomNavItem.Dashboard,
        BottomNavItem.Chat,
        BottomNavItem.Creative,
        BottomNavItem.Auth
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBgBlack),
        bottomBar = {
            // Respect standard Material 3 safe drawing and navigation heights
            NavigationBar(
                modifier = Modifier
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .testTag("app_bottom_navigation"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                navItems.forEach { item ->
                    val isSelected = currentRoute == item.route
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = GoldPremium,
                            selectedTextColor = GoldPremium,
                            indicatorColor = GoldPremium.copy(alpha = 0.12f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                            unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        ),
                        modifier = Modifier.testTag(item.tag)
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            // 1. Dashboard screen (HOS Hub)
            composable("dashboard") {
                DashboardScreen(
                    checkIns = checkIns,
                    userEmail = userEmail,
                    isSyncing = isSyncing,
                    onNavigateTo = { route -> navController.navigate(route) },
                    onSync = { viewModel.syncDataWithCloud() }
                )
            }

            // 2. Chat screen (AMONE Guide AI multi-turn dialogs)
            composable("chat") {
                ChatScreen(
                    messages = chatMessages,
                    isGenerating = isGenerating,
                    useThinking = useThinking,
                    useSearch = useSearch,
                    useMaps = useMaps,
                    ttsEnabled = ttsEnabled,
                    lowLatencyEnabled = lowLatencyEnabled,
                    apiError = apiError,
                    onSendMessage = { viewModel.sendMessage(it) },
                    onToggleThinking = { viewModel.useThinking.value = it },
                    onToggleSearch = { viewModel.useSearch.value = it },
                    onToggleMaps = { viewModel.useMaps.value = it },
                    onToggleTts = { viewModel.ttsEnabled.value = it },
                    onToggleLowLatency = { viewModel.lowLatencyEnabled.value = it },
                    onClearHistory = { viewModel.clearChat() }
                )
            }

            // 3. Creative Studio screen (Pro Image Aspect Ratios, sizes & Veo video models)
            composable("creative") {
                CreativeWorkspaceScreen(
                    generatedImageB64 = generatedImageB64,
                    generatedVideoStatus = generatedVideoStatus,
                    isGenerating = isGenerating,
                    onGenerateImage = { prompt, size, aspect -> viewModel.generateImage(prompt, size, aspect) },
                    onGenerateVideo = { prompt, aspect -> viewModel.generateVideo(prompt, aspect) }
                )
            }

            // 4. Account Screen (Firebase details & Sync triggers)
            composable("auth") {
                AuthScreen(
                    userEmail = userEmail,
                    isSyncing = isSyncing,
                    isAuthLoading = isAuthLoading,
                    authErrorMessage = authErrorMessage,
                    localCheckInsCount = checkIns.size,
                    localBreathCount = breathSessions.size,
                    onSignUp = { email, password -> viewModel.signUpWithEmailAndPassword(email, password) },
                    onLogin = { email, password -> viewModel.signInWithEmailAndPassword(email, password) },
                    onSocialLogin = { provider, email -> viewModel.signInWithSocialProvider(provider, email) },
                    onLogout = { viewModel.logout() },
                    onSync = { viewModel.syncDataWithCloud() },
                    onTriggerSampleException = { viewModel.triggerSampleException(it) },
                    onTriggerNotification = { title, body -> viewModel.triggerLocalNotification(title, body) }
                )
            }

            // 5. Embodied Reality deep detail screen
            composable("embodied") {
                EmbodiedRealityScreen(
                    currentSomaReading = currentSomaReading,
                    isGenerating = isGenerating,
                    onLogCheckIn = { emotion, intensity, zone, note ->
                        viewModel.logCheckIn(emotion, intensity, zone, note)
                    }
                )
            }

            // 6. Regulation System deep detail screen
            composable("regulation") {
                RegulationScreen(
                    breathSessions = breathSessions,
                    onLogBreathSession = { seconds, pre, post ->
                        viewModel.logBreathSession(seconds, pre, post)
                    }
                )
            }

            // 7. State Alchemy deep detail screen
            composable("alchemy") {
                StateAlchemyScreen(
                    generatedMusicStatus = generatedMusicStatus,
                    isGenerating = isGenerating,
                    onGenerateMusic = { viewModel.generateMusic(it) }
                )
            }

            // 8. HerAURA deep detail screen
            composable("heraura") {
                HerAuraScreen(
                    onLogCycle = { phase, day, phys, emot ->
                        viewModel.logCycleDay(phase, day, phys, emot)
                    }
                )
            }
        }
    }
}
