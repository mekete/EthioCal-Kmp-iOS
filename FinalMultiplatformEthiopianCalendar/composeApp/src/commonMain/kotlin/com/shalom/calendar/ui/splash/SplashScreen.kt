package com.shalom.calendar.ui.splash

import androidx.compose.runtime.Composable

/**
 * Platform-specific animated splash screen.
 * Android uses Lottie animation, iOS can use native animations.
 */
@Composable
expect fun AnimatedSplashScreen(onSplashFinished: () -> Unit)
