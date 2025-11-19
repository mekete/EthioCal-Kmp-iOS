package com.shalom.calendar

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.shalom.calendar.data.permissions.PermissionManager
import com.shalom.calendar.data.preferences.Language
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.ui.converter.DateConverterScreen
import com.shalom.calendar.ui.event.EventScreen
import com.shalom.calendar.ui.holidaylist.CalendarItemListScreen
import com.shalom.calendar.ui.month.MonthCalendarScreen
import com.shalom.calendar.ui.more.MoreScreen
import com.shalom.calendar.ui.more.ThemeViewModel
import com.shalom.calendar.ui.onboarding.OnboardingScreen
import com.shalom.calendar.ui.theme.EthiopianCalendarTheme
import com.shalom.calendar.ui.theme.ThemeMode
import com.shalom.calendar.util.LocaleHelper
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModel()

    private val settingsPreferences: SettingsPreferences by inject()

    private val permissionManager: PermissionManager by inject()

    private var currentLanguage: Language? = null

    override fun attachBaseContext(newBase: Context) { // Get the saved language preference and wrap the context with it
        val language = runBlocking {
            try {
                SettingsPreferences(newBase).language.first()
            } catch (e: Exception) {
                Language.AMHARIC
            }
        }
        currentLanguage = language
        val wrappedContext = LocaleHelper.wrapContext(newBase, language)
        super.attachBaseContext(wrappedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Listen for language changes and recreate activity when language changes
        lifecycleScope.launch {
            settingsPreferences.language.collect { newLanguage ->
                if (currentLanguage != null && currentLanguage != newLanguage) {
                    recreate()
                }
            }
        }

        setContent {
            val appTheme by themeViewModel.appTheme.collectAsState()
            val themeMode by themeViewModel.themeMode.collectAsState()

            EthiopianCalendarTheme(appTheme = appTheme, themeMode = themeMode) {

                val scrim = MaterialTheme.colorScheme.surfaceContainer.toArgb()

                val isDarkTheme = when (themeMode) {
                    ThemeMode.LIGHT -> false
                    ThemeMode.DARK -> true
                    ThemeMode.SYSTEM -> isSystemInDarkTheme()
                }

                SideEffect {
                    enableEdgeToEdge(statusBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(scrim)
                    } else {
                        SystemBarStyle.light(scrim, scrim)
                    }, navigationBarStyle = if (isDarkTheme) {
                        SystemBarStyle.dark(scrim)
                    } else {
                        SystemBarStyle.light(scrim, scrim)
                    })
                }
                MainScreen(permissionManager = permissionManager, settingsPreferences = settingsPreferences)
            }
        }
    }

    override fun onResume() {
        super.onResume() // Refresh permission state when returning from settings
        permissionManager.refreshPermissionState()
    }
}

@Composable
fun MainScreen(
    permissionManager: PermissionManager, settingsPreferences: SettingsPreferences
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "month" // Extract base route (without parameters) for bottom nav selection
    val baseRoute = currentRoute.substringBefore("?")
    val bottomNavItems = getBottomNavItems()

    // Determine start destination by checking onboarding completion status
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val hasCompletedOnboarding = settingsPreferences.hasCompletedOnboarding.first()
        android.util.Log.d("MainActivity", "hasCompletedOnboarding: $hasCompletedOnboarding")
        startDestination = if (!hasCompletedOnboarding) "onboarding" else "splash"
        android.util.Log.d("MainActivity", "startDestination: $startDestination")
    }

    // Show loading while determining start destination
    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(bottomBar = { // Hide bottom navigation during onboarding and splash
        if (currentRoute != "onboarding" && currentRoute != "splash") {
            NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(icon = { Icon(item.icon, contentDescription = item.label) }, label = {
                        Text(text = item.label, maxLines = 1, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                    }, selected = baseRoute == item.route, onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
                }
            }
        }
    }) { padding ->
        NavHost(navController = navController, startDestination = startDestination!!, modifier = Modifier.padding(top = if (currentRoute == "onboarding") 0.dp else padding.calculateTopPadding() / 3, bottom = if (currentRoute == "onboarding") 0.dp else padding.calculateBottomPadding())) {

            composable("onboarding") {
                OnboardingScreen(onComplete = {
                    navController.navigate("splash") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                })
            }

            composable("splash") {
                AnimatedSplashScreen(onSplashFinished = {
                    navController.navigate("month") {
                        popUpTo("splash") { inclusive = true }
                    }
                })
            }
            composable("month") {
                MonthCalendarScreen(permissionManager = permissionManager, navController = navController)
            }
            composable(route = "event?selectedDate={selectedDate}&hasEvents={hasEvents}", arguments = listOf(navArgument("selectedDate") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            }, navArgument("hasEvents") {
                type = NavType.BoolType
                defaultValue = false
            })) { backStackEntry ->
                EventScreen(event = "", permissionManager = permissionManager, selectedDate = backStackEntry.arguments?.getString("selectedDate"), hasEvents = backStackEntry.arguments?.getBoolean("hasEvents") ?: false)
            }
            composable("holiday") {
                CalendarItemListScreen()
            }
            composable("converter") {
                DateConverterScreen()
            }
            composable("more") {
                MoreScreen(permissionManager = permissionManager)
            }
        }
    }
}

data class BottomNavItem(
    val route: String, val label: String, val icon: ImageVector
)

@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(BottomNavItem("month", stringResource(R.string.nav_month), Icons.Default.CalendarMonth), BottomNavItem("event", stringResource(R.string.nav_event), Icons.Default.ViewDay), BottomNavItem("holiday", stringResource(R.string.nav_holiday), Icons.Default.ViewAgenda), BottomNavItem("converter", stringResource(R.string.nav_convert), Icons.Default.SwapHorizontalCircle), BottomNavItem("more", stringResource(R.string.nav_more), Icons.Default.Settings))
}

@Composable
fun AnimatedSplashScreen(onSplashFinished: () -> Unit) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_splash_anim))
    val progress by animateLottieCompositionAsState(composition = composition, iterations = 1)

    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onSplashFinished()
        }
    }

    Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceContainer), contentAlignment = Alignment.Center) {
        LottieAnimation(composition = composition, progress = { progress }, modifier = Modifier.size(250.dp))
    }
}
