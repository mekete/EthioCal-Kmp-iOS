package com.shalom.calendar.ui.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.shalom.calendar.presentation.onboarding.OnboardingViewModel
import com.shalom.calendar.presentation.onboarding.TOTAL_ONBOARDING_PAGES
import com.shalom.calendar.shared.resources.Res
import com.shalom.calendar.shared.resources.cd_back
import com.shalom.calendar.shared.resources.onboarding_done
import com.shalom.calendar.shared.resources.onboarding_next
import com.shalom.calendar.shared.resources.onboarding_skip
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val pagerState = rememberPagerState(pageCount = { TOTAL_ONBOARDING_PAGES })
    val scope = rememberCoroutineScope()

    // Sync pager state with view model
    LaunchedEffect(pagerState.currentPage) {
        viewModel.setCurrentPage(pagerState.currentPage)
    }

    // Handle navigation when onboarding is completed
    LaunchedEffect(state.isCompleted) {
        if (state.isCompleted) {
            onComplete()
        }
    }

    Scaffold(
        topBar = {
            if (state.currentPage > 0) {
                OnboardingTopBar(
                    onBackClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(
                                page = pagerState.currentPage - 1,
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                    stiffness = Spring.StiffnessLow
                                )
                            )
                        }
                    },
                    onSkipClick = {
                        viewModel.skipOnboarding()
                    }
                )
            }
        },
        bottomBar = {
            if (state.currentPage > 0) {
                OnboardingBottomBar(
                    currentPage = state.currentPage,
                    totalPages = TOTAL_ONBOARDING_PAGES,
                    onPageClick = { pageIndex ->
                        scope.launch {
                            pagerState.animateScrollToPage(pageIndex)
                        }
                    },
                    onNextClick = {
                        scope.launch {
                            if (pagerState.currentPage < TOTAL_ONBOARDING_PAGES - 1) {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            } else {
                                viewModel.completeOnboarding()
                            }
                        }
                    },
                    isLastPage = state.currentPage == TOTAL_ONBOARDING_PAGES - 1
                )
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            userScrollEnabled = false // Disable swipe, only use buttons
        ) { page ->
            when (page) {
                0 -> WelcomePage(
                    onGetStarted = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )

                1 -> LanguageSelectionPage(
                    selectedLanguage = state.selectedLanguage,
                    onLanguageSelected = viewModel::setLanguage
                )

                2 -> ThemeSelectionPage(
                    selectedTheme = state.selectedTheme,
                    onThemeSelected = viewModel::setTheme
                )

                3 -> HolidaySelectionPage(
                    showPublicHolidays = state.showPublicHolidays,
                    showOrthodoxHolidays = state.showOrthodoxHolidays,
                    showMuslimHolidays = state.showMuslimHolidays,
                    showOrthodoxDayNames = state.showOrthodoxDayNames,
                    onPublicHolidaysChanged = viewModel::setPublicHolidays,
                    onOrthodoxHolidaysChanged = viewModel::setOrthodoxHolidays,
                    onMuslimHolidaysChanged = viewModel::setMuslimHolidays,
                    onOrthodoxDayNamesChanged = viewModel::setOrthodoxDayNames
                )

                4 -> CalendarDisplayPage(
                    primaryCalendar = state.primaryCalendar,
                    displayDualCalendar = state.displayDualCalendar,
                    onPrimaryCalendarChanged = viewModel::setPrimaryCalendar,
                    onDualCalendarChanged = viewModel::setDisplayDualCalendar
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OnboardingTopBar(
    onBackClick: () -> Unit,
    onSkipClick: () -> Unit
) {
    TopAppBar(
        title = { },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(Res.string.cd_back)
                )
            }
        },
        actions = {
            TextButton(onClick = onSkipClick) {
                Text(text = stringResource(Res.string.onboarding_skip))
            }
        }
    )
}

@Composable
private fun OnboardingBottomBar(
    currentPage: Int,
    totalPages: Int = TOTAL_ONBOARDING_PAGES,
    onPageClick: (Int) -> Unit,
    onNextClick: () -> Unit,
    isLastPage: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Progress indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(totalPages - 1) { index -> // Exclude welcome page from indicator
                val pageIndex = index + 1
                Box(
                    modifier = Modifier
                        .width(if (pageIndex == currentPage) 24.dp else 8.dp)
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            if (pageIndex == currentPage) {
                                MaterialTheme.colorScheme.primary
                            } else if (pageIndex < currentPage) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                            }
                        )
                        .clickable { onPageClick(pageIndex) }
                )
            }
        }

        // Next/Done button
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = if (isLastPage) {
                    stringResource(Res.string.onboarding_done)
                } else {
                    stringResource(Res.string.onboarding_next)
                },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
