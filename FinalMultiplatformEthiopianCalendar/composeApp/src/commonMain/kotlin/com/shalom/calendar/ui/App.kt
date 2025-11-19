package com.shalom.calendar.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.shalom.calendar.data.preferences.SettingsPreferences
import com.shalom.calendar.ui.converter.DateConverterScreen
import com.shalom.calendar.ui.event.EventScreen
import com.shalom.calendar.ui.holidaylist.CalendarItemListScreen
import com.shalom.calendar.ui.month.MonthCalendarScreen
import com.shalom.calendar.ui.more.MoreScreen
import com.shalom.calendar.ui.navigation.Routes
import com.shalom.calendar.ui.navigation.getBottomNavItems
import com.shalom.calendar.ui.onboarding.OnboardingScreen
import com.shalom.calendar.ui.splash.AnimatedSplashScreen
import com.shalom.calendar.util.AppInfo
import com.shalom.calendar.util.ShareManager
import com.shalom.calendar.util.UrlLauncher
import kotlinx.coroutines.flow.first

@Composable
fun App(
    settingsPreferences: SettingsPreferences,
    urlLauncher: UrlLauncher,
    shareManager: ShareManager,
    appInfo: AppInfo
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: Routes.MONTH
    val baseRoute = currentRoute.substringBefore("?")
    val bottomNavItems = getBottomNavItems()

    // Determine start destination by checking onboarding completion status
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val hasCompletedOnboarding = settingsPreferences.hasCompletedOnboarding.first()
        startDestination = if (!hasCompletedOnboarding) Routes.ONBOARDING else Routes.SPLASH
    }

    // Show loading while determining start destination
    if (startDestination == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Scaffold(
        bottomBar = {
            // Hide bottom navigation during onboarding and splash
            if (currentRoute != Routes.ONBOARDING && currentRoute != Routes.SPLASH) {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceContainer) {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = {
                                Text(
                                    text = item.label,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = baseRoute == item.route,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = startDestination!!,
            modifier = Modifier.padding(
                top = if (currentRoute == Routes.ONBOARDING) 0.dp else padding.calculateTopPadding() / 3,
                bottom = if (currentRoute == Routes.ONBOARDING) 0.dp else padding.calculateBottomPadding()
            )
        ) {
            composable(Routes.ONBOARDING) {
                OnboardingScreen(
                    onComplete = {
                        navController.navigate(Routes.SPLASH) {
                            popUpTo(Routes.ONBOARDING) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.SPLASH) {
                AnimatedSplashScreen(
                    onSplashFinished = {
                        navController.navigate(Routes.MONTH) {
                            popUpTo(Routes.SPLASH) { inclusive = true }
                        }
                    }
                )
            }

            composable(Routes.MONTH) {
                MonthCalendarScreen(
                    onNavigateToEvent = { selectedDate, hasEvents ->
                        navController.navigate("${Routes.EVENT}?selectedDate=$selectedDate&hasEvents=$hasEvents")
                    }
                )
            }

            composable(
                route = "${Routes.EVENT}?selectedDate={selectedDate}&hasEvents={hasEvents}",
                arguments = listOf(
                    navArgument("selectedDate") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument("hasEvents") {
                        type = NavType.BoolType
                        defaultValue = false
                    }
                )
            ) { backStackEntry ->
                EventScreen(
                    selectedDate = backStackEntry.arguments?.getString("selectedDate"),
                    hasEvents = backStackEntry.arguments?.getBoolean("hasEvents") ?: false
                )
            }

            composable(Routes.HOLIDAY) {
                CalendarItemListScreen()
            }

            composable(Routes.CONVERTER) {
                DateConverterScreen()
            }

            composable(Routes.MORE) {
                MoreScreen(
                    urlLauncher = urlLauncher,
                    shareManager = shareManager
                )
            }
        }
    }
}
