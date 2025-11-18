package com.shalom.calendar.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * A reusable header component with navigation buttons (left and right) and center text.
 * This follows the pattern used in month header and holiday list header.
 *
 * @param centerText The text to display in the center
 * @param prevButtonLabel The text to display on the previous/left button
 * @param nextButtonLabel The text to display on the next/right button
 * @param onPrevClick Callback when previous/left button is clicked
 * @param onNextClick Callback when next/right button is clicked
 * @param onCenterClick Callback when center text is clicked (optional)
 * @param currentPage Current page/index for detecting navigation direction
 * @param modifier Modifier for the Row container
 * @param prevButtonEnabled Whether the previous button is enabled
 * @param nextButtonEnabled Whether the next button is enabled
 * @param centerTextStyle Text style for center text (defaults to titleLarge from month style)
 * @param centerFontWeight Font weight for center text (defaults to Medium)
 * @param centerTextColor Color for center text (defaults to onSurface)
 * @param centerMinWidth Minimum width for center text
 * @param contentDescriptionText Content description for accessibility
 */
@Composable
fun MonthHeaderItem(
    centerText: String,
    prevButtonLabel: String,
    nextButtonLabel: String,
    onPrevClick: () -> Unit,
    onNextClick: () -> Unit,
    onCenterClick: () -> Unit = {},
    currentPage: Int = 0,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(start = 16.dp, end = 16.dp, bottom = 0.dp, top = 8.dp),
    prevButtonEnabled: Boolean = true,
    nextButtonEnabled: Boolean = true,
    centerTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    centerFontWeight: FontWeight = FontWeight.Medium,
    centerTextColor: Color = Color.Unspecified,
    centerMinWidth: Dp? = null,
    contentDescriptionText: String? = null
) {
    // Track previous page to detect navigation direction
    // Direction: 1 = moving forward in time (next), -1 = moving backward in time (prev)
    val previousPageHolder = remember { mutableIntStateOf(currentPage) }

    // Compute direction synchronously during composition
    val slideDirection = if (currentPage > previousPageHolder.intValue) {
        1  // Going forward (next month) - slide left
    } else if (currentPage < previousPageHolder.intValue) {
        -1  // Going backward (prev month) - slide right
    } else {
        1  // Default for first render
    }

    // Update previous page after composition for next time
    SideEffect {
        previousPageHolder.intValue = currentPage
    }

    Row(
        modifier = if (contentDescriptionText != null) {
            modifier.semantics {
                contentDescription = contentDescriptionText
            }
        } else {
            modifier
        },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Prev button
        TextButton(onClick = onPrevClick, enabled = prevButtonEnabled) {
            AnimatedContent(
                targetState = prevButtonLabel,
                transitionSpec = {
                    if (slideDirection > 0) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "prevButtonAnimation"
            ) { label ->
                Text(label)
            }
        }

        // Center text with optional click handler
        AnimatedContent(
            targetState = centerText,
            transitionSpec = {
                if (slideDirection > 0) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }
            },
            label = "centerTextAnimation",
            modifier = Modifier
                .weight(1f)
                .then(if (centerMinWidth != null) {
                    Modifier.widthIn(min = centerMinWidth)
                } else {
                    Modifier
                })
        ) { text ->
            Text(
                text = text,
                style = centerTextStyle,
                fontWeight = centerFontWeight,
                color = if (centerTextColor == Color.Unspecified) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    centerTextColor
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCenterClick() }
            )
        }

        // Next button
        TextButton(onClick = onNextClick, enabled = nextButtonEnabled) {
            AnimatedContent(
                targetState = nextButtonLabel,
                transitionSpec = {
                    if (slideDirection > 0) {
                        slideInHorizontally { width -> width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> -width } + fadeOut()
                    } else {
                        slideInHorizontally { width -> -width } + fadeIn() togetherWith
                                slideOutHorizontally { width -> width } + fadeOut()
                    }
                },
                label = "nextButtonAnimation"
            ) { label ->
                Text(label)
            }
        }
    }
}
