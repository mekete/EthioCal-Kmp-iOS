package com.shalom.calendar.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material.icons.filled.ViewDay
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.shalom.calendar.shared.resources.Res
import com.shalom.calendar.shared.resources.nav_convert
import com.shalom.calendar.shared.resources.nav_event
import com.shalom.calendar.shared.resources.nav_holiday
import com.shalom.calendar.shared.resources.nav_month
import com.shalom.calendar.shared.resources.nav_more
import org.jetbrains.compose.resources.stringResource

/**
 * Bottom navigation item data class
 */
data class BottomNavItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

/**
 * Get the list of bottom navigation items
 */
@Composable
fun getBottomNavItems(): List<BottomNavItem> {
    return listOf(
        BottomNavItem(
            route = Routes.MONTH,
            label = stringResource(Res.string.nav_month),
            icon = Icons.Default.CalendarMonth
        ),
        BottomNavItem(
            route = Routes.EVENT,
            label = stringResource(Res.string.nav_event),
            icon = Icons.Default.ViewDay
        ),
        BottomNavItem(
            route = Routes.HOLIDAY,
            label = stringResource(Res.string.nav_holiday),
            icon = Icons.Default.ViewAgenda
        ),
        BottomNavItem(
            route = Routes.CONVERTER,
            label = stringResource(Res.string.nav_convert),
            icon = Icons.Default.SwapHorizontalCircle
        ),
        BottomNavItem(
            route = Routes.MORE,
            label = stringResource(Res.string.nav_more),
            icon = Icons.Default.Settings
        )
    )
}
